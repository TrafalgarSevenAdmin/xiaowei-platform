package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.EquipmentModified;

public interface EquipmentModifiedRepository extends BaseRepository<EquipmentModified>{

    EquipmentModified findByEquipmentNo(String code);

}
