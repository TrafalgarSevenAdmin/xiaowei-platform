package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
        EmptyUtils.assertString(equipmentId, "设备id不能为空");
        Optional<Equipment> equipmentOnBase = equipmentRepository.findById(equipmentId);
        EmptyUtils.assertOptional(equipmentOnBase, "没有查询到需要更新的对象");
        equipment.setId(equipmentId);
        equipmentRepository.save(equipment);
        return equipment;
    }
}
