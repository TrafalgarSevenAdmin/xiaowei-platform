package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.repository.EquipmentRepository;
import com.xiaowei.worksystem.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class EquipmentServiceImpl extends BaseServiceImpl<Equipment> implements IEquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(@Qualifier("equipmentRepository")BaseRepository repository) {
        super(repository);
    }


}
