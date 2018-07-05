package com.xiaowei.worksystem.service.impl;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.EquipmentModified;
import com.xiaowei.worksystem.entity.WorkOrder;
import com.xiaowei.worksystem.repository.EquipmentModifiedRepository;
import com.xiaowei.worksystem.service.IEquipmentModifiedService;
import com.xiaowei.worksystem.service.IEquipmentService;
import com.xiaowei.worksystem.service.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;


/**
 * 待修改的设备服务
 */
@Service
public class EquipmentModifiedServiceImpl extends BaseServiceImpl<EquipmentModified> implements IEquipmentModifiedService{

    @Autowired
    private EquipmentModifiedRepository equipmentModifiedRepository;

    @Autowired
    private IEquipmentService equipmentService;

    @Autowired
    private IWorkOrderService workOrderService;

    public EquipmentModifiedServiceImpl(@Qualifier("equipmentModifiedRepository")BaseRepository repository) {
        super(repository);
    }

    /**
     * 提交修改后设备信息供审核
     * @param workOrderId
     * @param equipment
     */
    @Override
    public void commitModified(String workOrderId, EquipmentModified equipment) {
        WorkOrder workOrder = workOrderService.findById(workOrderId);
        if (workOrder == null) {
            throw new BusinessException("工单不存在！");
        }
        equipment.setCreatedTime(new Date());
        equipment.setWorkOrder(workOrder);
        //设置工程师
        equipment.setEngineer(workOrder.getEngineer());
        EquipmentModified byCode = equipmentModifiedRepository.findByCode(equipment.getCode());
        if (byCode == null || byCode.getEngineer().getId().equals(workOrderId)) {
            equipmentModifiedRepository.save(equipment);
        } else {
            throw new BusinessException("该设备以由其他工单处理人员提交了修改请求，请稍后提交");
        }
    }

    @Override
    @Transactional
    public void passModified(String modifiedId) {
        Optional<EquipmentModified> toModifiedEquipment = equipmentModifiedRepository.findById(modifiedId);//获取待修改设备信息
        EmptyUtils.assertOptional(toModifiedEquipment, "没有找到此条修改信息");
        EquipmentModified equipmentModified = toModifiedEquipment.get();//获取数据
        Equipment modified = BeanCopyUtils.copy(equipmentModified, Equipment.class);//将修改的数据拷贝到最终设备里
        modified.setId(equipmentModified.getWorkOrder().getEquipment().getId());//修改最终设备的id
        equipmentService.saveEquipment(modified);//修改
        equipmentModifiedRepository.delete(equipmentModified);//删掉此条申请修改信息，因为已经修改成功了
    }
}
