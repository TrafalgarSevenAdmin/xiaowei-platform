package com.xiaowei.worksystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.EngineerWorkRepository;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IWorkOrderService;
import com.xiaowei.worksystem.status.ServiceItemStatus;
import com.xiaowei.worksystem.status.WorkOrderEngineerStatus;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import com.xiaowei.worksystem.status.WorkOrderUserStatus;
import com.xiaowei.worksystem.utils.ServiceItemUtils;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public WorkOrderServiceImpl(@Qualifier("workOrderRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public WorkOrder saveWorkOrder(WorkOrder workOrder) {
        //判定参数是否合规
        judgeAttribute(workOrder, JudgeType.INSERT);
        workOrderRepository.save(workOrder);
        return workOrder;
    }

    private void judgeAttribute(WorkOrder workOrder, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            workOrder.setId(null);
            workOrder.setCode(StringPYUtils.getSpellCode(workOrder.getServiceType()));
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
            Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
            EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        }
    }

    private void setSaveWorkOrderStatus(WorkOrder workOrder) {
        //如果没有指定工程师,则为待指派状态,否则为待接单状态
        if (workOrder.getEngineer() == null) {
            workOrder.setSystemStatus(WorkOrderSystemStatus.ASSIGNED.getStatus());//后台状态为待指派
            workOrder.setEngineerStatus(null);//工程师状态为空
            workOrder.setUserStatus(WorkOrderUserStatus.ASSIGNED.getStatus());//用户状态为待指派
        } else {
            workOrder.setSystemStatus(WorkOrderSystemStatus.INHAND.getStatus());//后台状态为处理中
            workOrder.setEngineerStatus(WorkOrderEngineerStatus.RECEIVED.getStatus());//工程师状态为待接单
            workOrder.setUserStatus(WorkOrderUserStatus.RECEIVED.getStatus());//用户状态为待接单
        }
    }

    private void judgeEquipment(WorkOrder workOrder) {
        Equipment equipment = workOrder.getEquipment();
        if (equipment == null) {
            return;
        }
        String code = equipment.getCode();
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
    public WorkOrder updateWorkOrder(WorkOrder workOrder) {
        //判定参数是否合规
        judgeAttribute(workOrder, JudgeType.UPDATE);
        workOrderRepository.save(workOrder);
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
        workOrder.setUserStatus(WorkOrderUserStatus.COMPLETED.getStatus());
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.COMPLETED.getStatus());
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
        //如果已经完成,则不允许修改
        if (workOrder.getUserStatus().equals(WorkOrderUserStatus.COMPLETED.getStatus())) {
            throw new BusinessException("该工单已经完成!");
        }
        //待确认
        if (!workOrder.getUserStatus().equals(WorkOrderUserStatus.CONFIRMED.getStatus())) {
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
        //如果已经完成,则不允许修改
        if (workOrder.getUserStatus().equals(WorkOrderUserStatus.COMPLETED.getStatus())) {
            throw new BusinessException("该工单已经完成!");
        }
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
    public WorkOrder receivedWorkOrder(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //待接单
        if (!workOrder.getEngineerStatus().equals(WorkOrderEngineerStatus.RECEIVED.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = new EngineerWork();//新建工程师处理工单附表
        engineerWork.setReceivedTime(new Date());
        engineerWorkRepository.save(engineerWork);
        workOrder.setEngineerWork(engineerWork);
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.APPOINTING.getStatus());//工程师状态变更为预约中
        workOrder.setUserStatus(WorkOrderUserStatus.INHAND.getStatus());//用户状态变更为处理中
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
        //待接单
        if (!workOrder.getEngineerStatus().equals(WorkOrderEngineerStatus.APPOINTING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setAppointTime(new Date());//预约时间
        engineerWorkRepository.save(engineerWork);
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.DEPARTED.getStatus());//工程师状态变更为预约中
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
        //待接单
        if (!workOrder.getEngineerStatus().equals(WorkOrderEngineerStatus.DEPARTED.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setDeparteTime(new Date());//出发时间
        engineerWork.setStartShape(shape);//出发地
        engineerWorkRepository.save(engineerWork);
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.TRIPING.getStatus());//工程师状态变更为行程中
        return workOrderRepository.save(workOrder);
    }

    /**
     * 工程师开始处理
     *
     * @param workOrderId
     * @param shape
     * @return
     */
    @Override
    @Transactional
    public WorkOrder inhandWorkOrder(String workOrderId, Geometry shape) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //行程中
        if (!workOrder.getEngineerStatus().equals(WorkOrderEngineerStatus.TRIPING.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setBeginInhandTime(new Date());//开始处理时间
        engineerWork.setArriveShape(shape);//目的地
        engineerWorkRepository.save(engineerWork);
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.INHAND.getStatus());//工程师状态变更为处理中
        return workOrderRepository.save(workOrder);
    }

    /**
     * 工程师处理完成
     *
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional
    public WorkOrder completeInhand(String workOrderId) {
        Optional<WorkOrder> one = workOrderRepository.findById(workOrderId);
        EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        WorkOrder workOrder = one.get();
        //处理中
        if (!workOrder.getEngineerStatus().equals(WorkOrderEngineerStatus.INHAND.getStatus())) {
            throw new BusinessException("状态错误!");
        }
        //检查服务项目是否已经全部完成
        judgeServiceItemIsDone(workOrder);
        workOrder.setEngineerStatus(WorkOrderEngineerStatus.COMPLETEINHAND.getStatus());//工程师状态为处理完成
        workOrder.setSystemStatus(WorkOrderSystemStatus.COMPLETEINHAND.getStatus());//后台状态为处理完成
        workOrder.setUserStatus(WorkOrderUserStatus.PAIED.getStatus());//用户状态为待付费
        EngineerWork engineerWork = workOrder.getEngineerWork();
        EmptyUtils.assertObject(engineerWork, "工程师处理工单对象为空");
        engineerWork.setEndInhandTime(new Date());//处理完成时间
        engineerWorkRepository.save(engineerWork);
        return workOrderRepository.save(workOrder);
    }

    /**
     * 检查服务项目是否已经全部完成
     *
     * @param workOrder
     */
    private void judgeServiceItemIsDone(WorkOrder workOrder) {
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderId(workOrder.getId());
        serviceItems.stream().forEach(serviceItem -> {
            if (!ArrayUtils.contains(ServiceItemUtils.isDone, serviceItem.getStatus())) {
                throw new BusinessException("第" + serviceItem.getOrderNumber() + "步服务项目:" + serviceItem.getServiceType() + "未完成!");
            }
        });

    }

    private void onPaied(WorkOrder workOrder) {
        workOrder.setUserStatus(WorkOrderUserStatus.EVALUATED.getStatus());
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
        workOrder.setUserStatus(WorkOrderUserStatus.INHAND.getStatus());//用户状态变更为处理中
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
