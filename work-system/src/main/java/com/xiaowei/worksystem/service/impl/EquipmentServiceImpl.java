package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.service.IEquipmentService;
import com.xiaowei.worksystem.status.EquipmentStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (StringUtils.isNotEmpty(equipment.getCode())) {
            Equipment byCode = equipmentRepository.findByCode(equipment.getCode());
            //若查不到此编号或此编号属于当前设备，那么就可以更新，否者就抛错
            if (!(byCode == null || byCode.getId().equals(equipmentId))) {
                throw new BusinessException("设备编号重复!");
            }
        }
        equipmentRepository.save(equipment);
        return equipment;
    }

    /**
     * 伪删除
     *
     * @param equipmentId
     */
    @Override
    @Transactional
    public void fakeDelete(String equipmentId) {
        EmptyUtils.assertString(equipmentId, "没有传入对象id");
        Optional<Equipment> one = equipmentRepository.findById(equipmentId);
        EmptyUtils.assertOptional(one, "没有查询到需要删除的对象");
        Equipment equipment = one.get();
        equipment.setStatus(EquipmentStatus.DELETE.getStatus());
        equipmentRepository.save(equipment);
    }

    @Override
    public Equipment saveEquipment(Equipment equipment) {
        if (StringUtils.isNotEmpty(equipment.getCode())) {
            Equipment byCode = equipmentRepository.findByCode(equipment.getCode());
            EmptyUtils.assertObjectNotNull(byCode,"设备编号重复!");
        }
        return equipmentRepository.save(equipment);
    }
}
