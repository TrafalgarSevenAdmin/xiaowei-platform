package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.ServiceItem;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.repository.ServiceItemRepository;
import com.xiaowei.worksystem.repository.WorkOrderRepository;
import com.xiaowei.worksystem.service.IWorkOrderService;
import com.xiaowei.worksystem.status.ServiceItemStatus;
import com.xiaowei.worksystem.status.WorkOrderEngineerStatus;
import com.xiaowei.worksystem.status.WorkOrderSystemStatus;
import com.xiaowei.worksystem.status.WorkOrderUserStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            workOrder.setSystemStatus(WorkOrderSystemStatus.INHAND.getStatus());//后台状态为待指派
            workOrder.setEngineerStatus(WorkOrderEngineerStatus.RECEIVED.getStatus());//工程师状态为待接单
            workOrder.setUserStatus(WorkOrderUserStatus.RECEIVED.getStatus());//用户状态为待指派
        }
    }

    private void judgeEquipment(WorkOrder workOrder) {
        Equipment equipment = workOrder.getEquipment();
        EmptyUtils.assertObject(equipment, "工单所属设备为空");
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
        EmptyUtils.assertOptional(one, "没有查询到需要修改的用户");
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
        EmptyUtils.assertOptional(one, "没有查询到需要修改的用户");
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
        workOrder.setUserStatus(WorkOrderUserStatus.INHAND.getStatus());
        List<ServiceItem> serviceItems = serviceItemRepository.findByWorkOrderIdAndStatus(workOrder.getId(), ServiceItemStatus.CONFIRMED.getStatus());
        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }
        serviceItems.stream().forEach(serviceItem -> {
            String serviceItemId = serviceItem.getId();
            //如果匹配上,则表示是用户确认的项目
            if (serviceItemIds.contains(serviceItemId)) {
                serviceItem.setStatus(ServiceItemStatus.NORMAL.getStatus());
                serviceItemRepository.save(serviceItem);
            }
        });
    }
}
