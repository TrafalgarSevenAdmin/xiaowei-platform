package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.service.IEquipmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class EquipmentServiceImpl extends BaseServiceImpl<Equipment> implements IEquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(@Qualifier("equipmentRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    public Equipment update(String equipmentId, Equipment equipment) {
        equipment.setId(equipmentId);
        EmptyUtils.assertString(equipmentId, "设备id不能为空");
        Optional<Equipment> equipmentOnBase = equipmentRepository.findById(equipmentId);
        EmptyUtils.assertOptional(equipmentOnBase, "没有查询到需要更新的对象");
        if (StringUtils.isNotEmpty(equipment.getEquipmentNo())) {
            Equipment byCode = equipmentRepository.findByEquipmentNo(equipment.getEquipmentNo());
            //若查不到此编号或此编号属于当前设备，那么就可以更新，否者就抛错
            if (!(byCode == null || byCode.getId().equals(equipmentId))) {
                throw new BusinessException("设备编号重复!");
            }
        }
        equipmentRepository.save(equipment);
        return equipment;
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        equipment.setCreatedTime(new Date());
        if (StringUtils.isNotEmpty(equipment.getEquipmentNo())) {
            Equipment byCode = equipmentRepository.findByEquipmentNo(equipment.getEquipmentNo());
            EmptyUtils.assertObjectNotNull(byCode,"设备编号重复!");
        }
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<Equipment> findBycustomerId(String customerId) {
        return equipmentRepository.findByCustomer_Id(customerId);
    }
}
