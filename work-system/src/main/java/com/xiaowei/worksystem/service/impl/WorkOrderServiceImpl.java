package com.xiaowei.worksystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.constant.TaskType;
import com.xiaowei.mq.sender.MessagePushSender;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.EngineerWorkRepository;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.repository.flow.WorkFlowItemRepository;
import com.xiaowei.worksystem.service.IWorkOrderService;
import com.xiaowei.worksystem.status.ServiceItemSource;
import com.xiaowei.worksystem.status.ServiceItemStatus;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import com.xiaowei.worksystem.status.WorkOrderUserStatus;
import com.xiaowei.worksystem.utils.ServiceItemUtils;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrder> implements IWorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private ServiceItemRepository serviceItemRepository;
    @Autowired
    private EngineerWorkRepository engineerWorkRepository;
    @Autowired
    private WorkFlowItemRepository workFlowItemRepository;
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * 消息发送服务
     */
    @Autowired
    private MessagePushSender messagePushSender;

    public WorkOrderServiceImpl(@Qualifier("workOrderRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public WorkOrder saveWorkOrder(WorkOrder workOrder, String workFlowId) {
        //判定参数是否合规
        judgeAttribute(workOrder, JudgeType.INSERT);
        workOrderRepository.save(workOrder);
        if (StringUtils.isNotEmpty(workFlowId)) {
            //设置服务项目
            setServiceItems(workOrder, workFlowId);
        }
        return workOrder;
    }

    private void judgeAttribute(WorkOrder workOrder, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            workOrder.setId(null);
            workOrder.setCode(getCurrentDayMaxCode());
            workOrder.setEvaluate(null);
            workOrder.setEngineerWork(null);
            workOrder.setCreatedTime(new Date());
            //检查设备,如果没有设备,则新增设备
            judgeEquipment(workOrder);
            //设置工单状态
            setSaveWorkOrderStatus(workOrder);

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String workOrderId = workOrder.getId();
            EmptyUtils.assertString(workOrderId, "没有传入对象id");
            Optional<WorkOrder> optional = workOrderRepository.findById(workOrderId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
            final WorkOrder one = optional.get();
            //设置无法修改的字段
            workOrder.setRepairFileStore(one.getRepairFileStore());//报修图片id无法修改

        }
    }

    /**
     * 获取当天最大的工单编号
     *
     * @return
     */
    private String getCurrentDayMaxCode() {
        String code = "FWGD" + DateUtils.getCurrentDate();
        String incr;
        ShardedJedis resource = shardedJedisPool.getResource();
        if (!resource.exists(code)) {
            resource.setex(code, 86400, "1");//24小时有效期
            incr = 1 + "";
        } else {
            if (999 == Long.valueOf(resource.get(code))) {
                throw new BusinessException("编码数量超出范围");
            } else {
                incr = resource.incr(code) + "";
            }
        }
        int len = 4 - incr.length();
        for (int i = 0; i < len; i++) {
            incr = "0" + incr;
        }
        return code + incr;
    }

    private void setSaveWorkOrderStatus(WorkOrder workOrder) {
        //如果没有指定工程师,则为待指派状态,否则为待接单状态
        if (workOrder.getEngineer() == null) {
            workOrder.setSystemStatus(WorkOrderSystemStatus.DISTRIBUTE.getStatus());//后台状态为待指派
            workOrder.setUserStatus(WorkOrderUserStatus.NORMAO.getStatus());//用户状态为正常
        } else {
            workOrder.setSystemStatus(WorkOrderSystemStatus.RECEIVE.getStatus());//后台状态为待接单
            workOrder.setUserStatus(WorkOrderUserStatus.NORMAO.getStatus());//用户状态为待接单
        }
    }

    private void judgeEquipment(WorkOrder workOrder) {
        Equipment equipment = workOrder.getEquipment();
        if (equipment == null) {
            return;
        }
        String code = equipment.getEquipmentNo();
        Equipment byCode = equipmentRepository.findByCode(code);
        if (byCode == null) {
            //如果没有设备,则新增一个设备
            workOrder.setEquipment(equipmentRepository.save(equipment));
        } else {
            workOrder.setEquipment(byCode);
        }
    }

    @Override
    @Transactional
    public WorkOrder updateWorkOrder(WorkOrder workOrder, String workFlowId) {
        //判定参数是否合规
        judgeAttribute(workOrder, JudgeType.UPDATE);
        workOrderRepository.save(workOrder);
        if (StringUtils.isNotEmpty(workFlowId)) {
            //先删除服务项目
            serviceItemRepository.deleteByWorkOrderId(workOrder.getId());
            //再重新保存服务项目
            setServiceItems(workOrder, workFlowId);
        }
        return workOrder;
    }

    /**
     * 伪删除
     *
     * @param workOrderId
     */
    @Override
    @Transactional
    public void fakeDelete(String workOrderId) {
        EmptyUtils.assertString(workOrderId, "没有传入对象id");
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要删除的对象");
        WorkOrder workOrder = one.get();
        workOrder.setUserStatus(WorkOrderUserStatus.NORMAO.getStatus());
        workOrder.setSystemStatus(WorkOrderSystemStatus.DELETE.getStatus());
        workOrderRepository.save(workOrder);
    }

    /**
     * 用户确认项目
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder confirmed(String workOrderId, List<String> serviceItemIds) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();

        //待确认
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.AFFIRM.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        onConfirmed(workOrder, serviceItemIds);
        workOrderRepository.save(workOrder);
        return workOrder;
    }

    /**
     * 用户付费项目
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder payServiceItem(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //待付款
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.PAIED.getStatus())) {
            throw new BusinessException("状态错误!");
        }

        onPaied(workOrder);
        workOrderRepository.save(workOrder);
        return workOrder;

    }

    /**
     * 工程师接单
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder receivedWorkOrder(String workOrderId, Boolean receive) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        if (receive) {//同意接单
            //待接单
            if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.RECEIVE.getStatus())) {
                throw new BusinessException("状态错误!");
            }
            EngineerWork engineerWork = new EngineerWork();//新建工程师处理工单附表
            engineerWork.setReceivedTime(new Date());
            engineerWorkRepository.save(engineerWork);
            workOrder.setEngineerWork(engineerWork);
            workOrder.setSystemStatus(WorkOrderSystemStatus.APPOINTING.getStatus());//系统状态变更为预约中
        } else {//拒绝接单
            workOrder.setEngineer(null);
            workOrder.setBackgrounder(null);
            workOrder.setSystemStatus(WorkOrderSystemStatus.DISTRIBUTE.getStatus());//系统状态变更为待派发
            //删除工单服务项目
            serviceItemRepository.deleteByWorkOrderId(workOrder.getId());
        }

        return workOrderRepository.save(workOrder);
    }

    /**
     * 工程师预约
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder appointingWorkOrder(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //预约中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.APPOINTING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setAppointTime(new Date());//预约时间
        engineerWorkRepository.save(engineerWork);
        workOrder.setSystemStatus(WorkOrderSystemStatus.DEPART.getStatus());//工程师状态变更为待出发
        return workOrderRepository.save(workOrder);
    }

    /**
     * 工程师出发
     *
     * @param workOrderId
     * @param shape
     * @return
     */
    @Override
    @Transactional
    public WorkOrder departeWorkOrder(String workOrderId, Geometry shape) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //预约中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.DEPART.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setDeparteTime(new Date());//出发时间
        engineerWork.setStartShape(shape);//出发地
        engineerWorkRepository.save(engineerWork);
        workOrder.setSystemStatus(WorkOrderSystemStatus.TRIPING.getStatus());//工程师状态变更为行程中
        return workOrderRepository.save(workOrder);
    }

    /**
     * 工程师开始处理
     *
     * @param workOrderId
     * @param shape
     * @param arriveFileStore
     * @return
     */
    @Override
    @Transactional
    public WorkOrder inhandWorkOrder(String workOrderId, Geometry shape, String arriveFileStore) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //行程中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.TRIPING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setBeginInhandTime(new Date());//开始处理时间
        engineerWork.setArriveShape(shape);//目的地
        engineerWork.setArriveFileStore(arriveFileStore);//到达图片
        val firstOrderNumber = setFirstServiceItem(workOrderId);
        engineerWorkRepository.save(engineerWork);
        workOrder.setCurrentOrderNumber(firstOrderNumber);//当前处理步骤
        workOrder.setSystemStatus(WorkOrderSystemStatus.INHAND.getStatus());//工程师状态变更为处理中
        return workOrderRepository.save(workOrder);
    }

    /**
     * 递归查询下一服务项目
     *
     * @param serviceItem
     * @return
     */
    @SuppressWarnings("all")
    private ServiceItem findNextItem(ServiceItem serviceItem) {
        Integer orderNumber = serviceItem.getOrderNumber();
        ServiceItem nextItem = serviceItemRepository.findByWorkOrderIdAndOrderNumber(serviceItem.getWorkOrder().getId(), ++orderNumber);
        if (nextItem == null) {
            return null;
        }
        if (nextItem.getStatus().equals(ServiceItemStatus.INEXECUTION.getStatus())) {//如果是不执行,则查询下一条
            return findNextItem(nextItem);
        }
        return nextItem;
    }

    /**
     * 设置第一个服务项目的开始处理时间
     *
     * @param workOrderId
     * @return 返回第一个需要处理的服务项目的排序号, 没有则返回0
     */
    private Integer setFirstServiceItem(String workOrderId) {
        ServiceItem serviceItem = serviceItemRepository.findByWorkOrderIdAndOrderNumber(workOrderId, 1);
        if (serviceItem == null) {//如果一条服务项目都没有,则返回
            return 0;
        }
        if (serviceItem.getStatus().equals(ServiceItemStatus.INEXECUTION.getStatus())) {//如果是不执行,则查询下一条
            serviceItem = findNextItem(serviceItem);
        }
        if (serviceItem == null) {//如果没有服务项目,则返回
            return 0;
        }
        serviceItem.setBeginTime(new Date());
        serviceItemRepository.save(serviceItem);
        return serviceItem.getOrderNumber();
    }

    /**
     * 工程师处理完成
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder finishInhand(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //处理中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.INHAND.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        //检查服务项目是否已经全部完成
        judgeServiceItemIsDone(workOrder);
        //检查是否需要设置为待付费状态
        judgeIsPaied(workOrder);

        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setEndInhandTime(new Date());//处理完成时间
        engineerWorkRepository.save(engineerWork);
        WorkOrder save = workOrderRepository.save(workOrder);
        //设置为24小时后自动完成此工单
        messagePushSender.sendDelayTask(new TaskMessage(workOrderId, TaskType.AUTO_PREPIGEONHOLE), 1000 * 60 * 60 * 24);
        return save;
    }

    /**
     * 检查是否需要设置为待付费状态
     *
     * @param workOrder
     */
    private void judgeIsPaied(WorkOrder workOrder) {
        workOrder.setSystemStatus(WorkOrderSystemStatus.FINISHHAND.getStatus());//工程师状态为处理完成
        val serviceItems = serviceItemRepository.findByWorkOrderIdAndStatus(workOrder.getId(), ServiceItemStatus.PAIED.getStatus());
        if (CollectionUtils.isEmpty(serviceItems)) {
            workOrder.setUserStatus(WorkOrderUserStatus.EVALUATED.getStatus());//待评价
        } else {
            workOrder.setUserStatus(WorkOrderUserStatus.PAIED.getStatus());//用户状态为待付费
        }

    }

    /**
     * 派单
     *
     * @param workOrder
     * @param workFlowId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder distributeWorkOrder(WorkOrder workOrder, String workFlowId) {
        //验证派单工单的属性
        judgeDistributeWorkOrder(workOrder);

        Optional<WorkOrder> optional = workOrderRepository.findById(workOrder.getId());
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        WorkOrder one = optional.get();
        //待派发
        if (!one.getSystemStatus().equals(WorkOrderSystemStatus.DISTRIBUTE.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        one.setSystemStatus(WorkOrderSystemStatus.RECEIVE.getStatus());//变更状态为待接单
        one.setEngineer(workOrder.getEngineer());
        one.setBackgrounder(workOrder.getBackgrounder());
        workOrderRepository.save(one);
        if (StringUtils.isNotEmpty(workFlowId)) {
            //设置服务项目
            setServiceItems(workOrder, workFlowId);
        }
        return one;
    }

    /**
     * 将工单变成待归档状态
     *
     * @param workOrderId
     */
    public void workOrderToPrepigeonhole(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的工单");
        WorkOrder workOrder = one.get();
        //处理中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus())) {
            throw new BusinessException("工单状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.PREPIGEONHOLE.getStatus());

        workOrderRepository.save(workOrder);
    }

    /**
     * 工单待归档
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder prePigeonhole(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //处理完成
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.PREPIGEONHOLE.getStatus());//工单状态变更为待归档
        return workOrderRepository.save(workOrder);
    }

    /**
     * 工单终审
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder pigeonholed(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //处理完成
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.PREPIGEONHOLE.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.PIGEONHOLED.getStatus());//工单状态变更为待归档
        return workOrderRepository.save(workOrder);
    }

    /**
     * 设置服务项目
     *
     * @param workOrder
     * @param workFlowId
     */
    private void setServiceItems(WorkOrder workOrder, String workFlowId) {
        val workFlowItems = workFlowItemRepository.findByWorkFlowId(workFlowId);
        if (CollectionUtils.isEmpty(workFlowItems)) {
            return;
        }
        List<ServiceItem> serviceItems = BeanCopyUtils.copyList(workFlowItems, ServiceItem.class);
        serviceItems.stream().forEach(serviceItem -> {
            serviceItem.setWorkOrder(workOrder);
            serviceItem.setStatus(ServiceItemStatus.NORMAL.getStatus());//状态正常
            serviceItem.setSource(ServiceItemSource.BACKGROUNDER.getStatus());//后台创建来源
            serviceItem.setCreatedTime(new Date());//创建时间
            serviceItemRepository.save(serviceItem);
        });
    }

    /**
     * 验证派单工单的属性
     *
     * @param workOrder
     */
    private void judgeDistributeWorkOrder(WorkOrder workOrder) {
        EmptyUtils.assertString(workOrder.getId(), "没有传入对象id");
        val engineer = workOrder.getEngineer();
        val backgrounder = workOrder.getBackgrounder();
        EmptyUtils.assertObject(engineer, "处理工程师为空");
        EmptyUtils.assertObject(backgrounder, "后台处理人为空");
        EmptyUtils.assertString(engineer.getId(), "处理工程师id为空");
        EmptyUtils.assertString(backgrounder.getId(), "后台处理人id为空");
    }

    /**
     * 检查服务项目是否已经全部完成
     *
     * @param workOrder
     */
    private void judgeServiceItemIsDone(WorkOrder workOrder) {
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderId(workOrder.getId());
        boolean paid = false;
        for (ServiceItem serviceItem : serviceItems) {
            if (!ArrayUtils.contains(ServiceItemUtils.isDone, serviceItem.getStatus())) {
                throw new BusinessException("第" + serviceItem.getOrderNumber() + "步服务项目:" + serviceItem.getServiceType() + "未完成!");
            }
            if (!paid) {
                //检查用户状态是否该设置为待付费
                if (serviceItem.getStatus().equals(ServiceItemStatus.PAIED.getStatus()) && serviceItem.getCharge()) {
                    workOrder.setUserStatus(WorkOrderUserStatus.PAIED.getStatus());
                    paid = true;
                }
            }
        }
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.PAIED.getStatus())) {//判断是否为付费状态,如果不是付费状态,则变更用户状态为待评价
            workOrder.setUserStatus(WorkOrderUserStatus.EVALUATED.getStatus());
        }

    }

    private void onPaied(WorkOrder workOrder) {
        workOrder.setUserStatus(WorkOrderUserStatus.EVALUATED.getStatus());//待评价
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderIdAndStatus(workOrder.getId(), ServiceItemStatus.PAIED.getStatus());
        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }
        serviceItems.stream().forEach(serviceItem -> {
            //所有项目由待付款变为完成状态
            serviceItem.setStatus(ServiceItemStatus.COMPLETED.getStatus());
            serviceItemRepository.save(serviceItem);
        });
    }

    private void onConfirmed(WorkOrder workOrder, List<String> serviceItemIds) {
        workOrder.setUserStatus(WorkOrderUserStatus.NORMAO.getStatus());//用户状态变更为正常
        //待确认的新增项目
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderIdAndStatus(workOrder.getId(), ServiceItemStatus.CONFIRMED.getStatus());
        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }
        if (serviceItemIds == null) {
            serviceItemIds = new ArrayList<>();
        }
        val finalServiceItemIds = serviceItemIds;
        serviceItems.stream().forEach(serviceItem -> {
            String serviceItemId = serviceItem.getId();
            //如果匹配上,则表示是用户确认的项目
            if (finalServiceItemIds.contains(serviceItemId)) {
                serviceItem.setStatus(ServiceItemStatus.NORMAL.getStatus());
                serviceItemRepository.save(serviceItem);
            } else {
                serviceItem.setStatus(ServiceItemStatus.INEXECUTION.getStatus());
                serviceItemRepository.save(serviceItem);
            }
        });
    }
}
