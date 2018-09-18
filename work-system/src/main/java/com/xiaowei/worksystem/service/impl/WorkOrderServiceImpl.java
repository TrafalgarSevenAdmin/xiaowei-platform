package com.xiaowei.worksystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.repository.SysUserRepository;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.DateUtils;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.mq.sender.MessagePushSender;
import com.xiaowei.pay.consts.PayStatus;
import com.xiaowei.pay.entity.XwOrder;
import com.xiaowei.pay.repository.XwOrderRepository;
import com.xiaowei.pay.status.XwType;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import com.xiaowei.worksystem.repository.*;
import com.xiaowei.worksystem.repository.flow.WorkFlowItemRepository;
import com.xiaowei.worksystem.service.IWorkOrderService;
import com.xiaowei.worksystem.status.*;
import com.xiaowei.worksystem.utils.ServiceItemUtils;
import com.xiaowei.worksystem.utils.WorkOrderUtils;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

@Slf4j
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
    @Autowired
    private XwOrderRepository orderRepository;
    @Autowired
    private RequestWorkOrderRepository requestWorkOrderRepository;
    @Autowired
    private SysUserRepository userRepository;

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
        switch (workOrder.getWorkOrderType().getServiceType()){
            case IN://内部工单
                workOrder.setCode(getCurrentDayMaxCode(StringPYUtils.cn2FirstSpell(workOrder.getWorkOrderType().getName())));
                workOrder.setSystemStatus(WorkOrderSystemStatus.FINISHHAND.getStatus());
                workOrderRepository.save(workOrder);
                ;break;
            case OUT:
                //判定参数是否合规
                judgeAttribute(workOrder, JudgeType.INSERT);
                workOrderRepository.save(workOrder);
                if (StringUtils.isNotEmpty(workFlowId)) {
                    WorkFlow workFlow = new WorkFlow();
                    workFlow.setId(workFlowId);
                    workOrder.setWorkFlow(workFlow);
                    //设置服务项目
                    setServiceItems(workOrder, workFlowId);
                }
                ;break;
        }
        return workOrder;
    }

    private void judgeAttribute(WorkOrder workOrder, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            workOrder.setId(null);
            workOrder.setCode(getCurrentDayMaxCode(StringPYUtils.cn2FirstSpell(workOrder.getWorkOrderType().getName())));
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
//            workOrder.setRepairFileStore(one.getRepairFileStore());//报修图片id无法修改

        }
    }

    /**
     * 获取当天最大的工单编号
     *
     * @return
     */
    private String getCurrentDayMaxCode(String spatial) {
        String code = spatial + DateUtils.getCurrentDate();
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
        final Optional<Equipment> optional = equipmentRepository.findById(equipment.getId());
        EmptyUtils.assertOptional(optional, "没有查询到该设备");
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
            WorkFlow workFlow = new WorkFlow();
            workFlow.setId(workFlowId);
            workOrder.setWorkFlow(workFlow);
            //再重新保存服务项目
            setServiceItems(workOrder, workFlowId);
        }
        return workOrder;
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
        log.info("5-----------------------------");
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //待付款
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.PAIED.getStatus())) {
            log.info("6-----------------------------");
            throw new BusinessException("状态错误!");
        }

        onPaied(workOrder);
        workOrderRepository.save(workOrder);
        log.info("10-----------------------------");
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
    public WorkOrder inhandWorkOrder(String workOrderId, Geometry shape, String arriveFileStore, Integer arriveStatus) {
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
        engineerWork.setArriveStatus(arriveStatus);//到达状态
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
        workOrder.setSystemStatus(WorkOrderSystemStatus.FINISHHAND.getStatus());//工程师状态为处理完成

        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setEndInhandTime(new Date());//处理完成时间
        engineerWorkRepository.save(engineerWork);
        WorkOrder save = workOrderRepository.save(workOrder);
//        //设置为24小时后自动完成此工单
//        messagePushSender.sendDelayTask(new TaskMessage(workOrderId, TaskType.AUTO_PREPIGEONHOLE), 1000 * 60 * 60 * 24);
        return save;
    }

    /**
     * 派单
     *
     * @param workOrder
     * @return
     */
    @Override
    @Transactional
    public WorkOrder distributeWorkOrder(WorkOrder workOrder) {
        //验证派单工单的属性
        judgeDistributeWorkOrder(workOrder);

        Optional<WorkOrder> optional = workOrderRepository.findById(workOrder.getId());
        EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        WorkOrder one = optional.get();
        //待派发待接单都可以派发
        if (!one.getSystemStatus().equals(WorkOrderSystemStatus.DISTRIBUTE.getStatus()) &&
                !one.getSystemStatus().equals(WorkOrderSystemStatus.RECEIVE.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        one.setSystemStatus(WorkOrderSystemStatus.RECEIVE.getStatus());//变更状态为待接单
        one.setEngineer(workOrder.getEngineer());
        one.setBackgrounder(workOrder.getBackgrounder());
        workOrderRepository.save(one);
        return one;
    }

//    /**
//     * 工单待归档
//     *
//     * @param workOrderId
//     * @return
//     */
//    @Override
//    @Transactional
//    public WorkOrder prePigeonhole(String workOrderId) {
//        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
//        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
//        WorkOrder workOrder = one.get();
//        //处理完成
//        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus())) {
//            throw new BusinessException("状态错误!");
//        }
//        workOrder.setSystemStatus(WorkOrderSystemStatus.PREPIGEONHOLE.getStatus());//工单状态变更为待归档
//        return workOrderRepository.save(workOrder);
//    }

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
        //工单处理完成
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus()) &&
                !workOrder.getSystemStatus().equals(WorkOrderSystemStatus.EXPENSEING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.PIGEONHOLED.getStatus());//工单状态变更为归档
        //终审人,终审时间
        workOrder.setPigeonholedTime(new Date());
        workOrder.setPigeonholedUser(userRepository.getOne(LoginUserUtils.getLoginUser().getId()));
        return workOrderRepository.save(workOrder);
    }

    @Override
    @Transactional
    public String createPay(String workOrderId) {
        Optional<WorkOrder> optional = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(optional, "没有查询到该工单");
        WorkOrder workOrder = optional.get();
        //待付费
        if (!WorkOrderUserStatus.PAIED.getStatus().equals(workOrder.getUserStatus())) {
            throw new BusinessException("工单状态错误!");
        }
        //先从订单表查询,如果有工单的支付订单,则返回该支付订单,否则新建返回
        List<XwOrder> xwOrders = orderRepository.findByBusinessIdAndType(workOrderId, XwType.WORKORDER.getStatus());
        XwOrder xwOrder;
        //如果之前的订单不存在或者订单已经超时了，就重新创建一个订单。之前的订单让他超时
        if (CollectionUtils.isEmpty(xwOrders) || xwOrders.get(0).getTimeExpire().getTime() < new Date().getTime()) {
            if (CollectionUtils.isNotEmpty(xwOrders)) {
                for (XwOrder oldXwOrder : xwOrders) {
                    oldXwOrder.setStatus(PayStatus.close);
                    oldXwOrder.setMessage("订单超时");
                    orderRepository.save(oldXwOrder);
                }
            }
            SysUser user = new SysUser();
            user.setId(LoginUserUtils.getLoginUser().getId());
            //省略根据工单计算订单金额,开发阶段默认1分钱
            xwOrder = new XwOrder(workOrder.getId(), user, "晓维快修-工单支付", 1, XwType.WORKORDER.getStatus());
            xwOrder = orderRepository.save(xwOrder);
        } else {
            xwOrder = xwOrders.get(0);
        }

        return xwOrder.getId();
    }

    /**
     * 当前登录用户查询工程师工单的各种状态
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getCountFromEngineer(String userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("receiveCount", workOrderRepository.findCountByEngineerIdAndStatusIn(userId, WorkOrderUtils.RECEIVE));
        dataMap.put("inhandCount", workOrderRepository.findCountByEngineerIdAndStatusIn(userId, WorkOrderUtils.INHAND));
        dataMap.put("finishedCount", workOrderRepository.findCountByEngineerIdAndStatusIn(userId, WorkOrderUtils.FINISHED));
        return dataMap;
    }

    @Override
    public Map<String, Object> getCountFromProposer(String userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("affirmCount", workOrderRepository.findCountByProposerIdAndUserStatus(userId, WorkOrderUserStatus.AFFIRM.getStatus()));
        dataMap.put("paiedCount", workOrderRepository.findCountByProposerIdAndUserStatus(userId, WorkOrderUserStatus.PAIED.getStatus()));
        dataMap.put("evaluatedCount", workOrderRepository.findCountByProposerIdAndUserStatus(userId, WorkOrderUserStatus.EVALUATED.getStatus()));
        return dataMap;
    }

    @Override
    public Map<String, Object> getCountFromBackgrounder(String userId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("preCreateCount", requestWorkOrderRepository.findCountByStatus(RequestWorkOrderStatus.UNTREATED.getStatus()));
        dataMap.put("distributeCount", workOrderRepository.findCountByBackgrounderAndStatus(userId, WorkOrderSystemStatus.DISTRIBUTE.getStatus()));
        dataMap.put("receiveCount", workOrderRepository.findCountByBackgrounderAndStatus(userId, WorkOrderSystemStatus.RECEIVE.getStatus()));
        dataMap.put("qualityCount", workOrderRepository.findCountByBackgrounderAndStatus(userId, WorkOrderSystemStatus.QUALITY.getStatus()));
        dataMap.put("finishedCount", workOrderRepository.findCountByBackgrounderAndStatusIn(userId, WorkOrderUtils.FINISHED));
        return dataMap;
    }

    /**
     * 报销中
     *
     * @param workOrderCode
     * @return
     */
    @Override
    @Transactional
    public WorkOrder expenseing(String workOrderCode) {
        WorkOrder workOrder = workOrderRepository.findByCode(workOrderCode);
        EmptyUtils.assertObject(workOrder, "没有查询到需要修改的对象");
        //工单处理完成或者报销中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.FINISHHAND.getStatus())&&
                !workOrder.getSystemStatus().equals(WorkOrderSystemStatus.EXPENSEING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.EXPENSEING.getStatus());//工单状态变更为报销中
        return workOrderRepository.save(workOrder);
    }

    /**
     * 报销完成
     *
     * @param workOrderCode
     * @return
     */
    @Override
    @Transactional
    public WorkOrder finishedExpense(String workOrderCode) {
        WorkOrder workOrder = workOrderRepository.findByCode(workOrderCode);
        EmptyUtils.assertObject(workOrder, "没有查询到需要修改的对象");
        //工单报销中
        if (!workOrder.getSystemStatus().equals(WorkOrderSystemStatus.EXPENSEING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        workOrder.setSystemStatus(WorkOrderSystemStatus.FINISHHAND.getStatus());//工单状态变更为处理完成
        return workOrderRepository.save(workOrder);
    }

    /**
     *  签到审核
     * @param engineerWorkId
     * @param pigeonholedStatus
     */
    @Override
    @Transactional
    public void pigeonholedStatus(String engineerWorkId, Integer pigeonholedStatus) {
        Optional<EngineerWork> optional = engineerWorkRepository.findById(engineerWorkId);
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        EngineerWork engineerWork = optional.get();
        engineerWork.setPigeonholedStatus(pigeonholedStatus);
        engineerWorkRepository.save(engineerWork);
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
        if (!paid) {//判断是否为付费状态,如果不是付费状态,则变更用户状态为待评价
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
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderIdAndStatusOrderByOrderNumber(workOrder.getId(), ServiceItemStatus.CONFIRMED.getStatus());
        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }
        if (serviceItemIds == null) {
            serviceItemIds = new ArrayList<>();
        }
        //正常状态的服务项目
        final List<ServiceItem> normalItem = serviceItemRepository.findByWorkOrderIdAndStatus(workOrder.getId(), ServiceItemStatus.NORMAL.getStatus());
        boolean isEmpty = CollectionUtils.isEmpty(normalItem);//是否还有正常状态的服务项目

        for (ServiceItem serviceItem : serviceItems) {
            String serviceItemId = serviceItem.getId();
            //如果匹配上,则表示是用户确认的项目
            if (serviceItemIds.contains(serviceItemId)) {
                serviceItem.setStatus(ServiceItemStatus.NORMAL.getStatus());
                if (isEmpty) {
                    serviceItem.setBeginTime(new Date());
                    workOrder.setCurrentOrderNumber(serviceItem.getOrderNumber());//设置当前处理步骤
                    isEmpty = false;
                }
                serviceItemRepository.save(serviceItem);
            } else {
                serviceItem.setStatus(ServiceItemStatus.INEXECUTION.getStatus());
                serviceItemRepository.save(serviceItem);
            }
        }
    }
}
