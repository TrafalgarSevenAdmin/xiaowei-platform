package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;


public interface IEquipmentService extends IBaseService<Equipment> {

    /**
     * 更新设备信息
     * @param equipmentId
     * @param equipment
     * @return
     */
    Equipment update(String equipmentId, Equipment equipment);
}
