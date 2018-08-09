package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.Equipment;

import java.util.List;

public interface EquipmentRepository extends BaseRepository<Equipment>{
    Equipment findByEquipmentNo(String code);

    List<Equipment> findByCustomer_Id(String customerId);
}
