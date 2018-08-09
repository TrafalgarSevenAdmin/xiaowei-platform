package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface IEquipmentService extends IBaseService<Equipment> {

    /**
     * 更新设备信息
     * @param equipmentId
     * @param equipment
     * @return
     */
    Equipment update(String equipmentId, Equipment equipment);

    /**
     * 保存设备
     * @param equipment
     * @return
     */
    Equipment saveEquipment(Equipment equipment);

    /**
     * 获取此服务对象下的所有设备
     * @param customerId
     * @return
     */
    List<Equipment> findBycustomerId(String customerId);
}
