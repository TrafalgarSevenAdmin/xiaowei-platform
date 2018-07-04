package com.xiaowei.worksystem.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.EquipmentModified;

/**
 * 待修改的设备服务
 */
public interface IEquipmentModifiedService extends IBaseService<EquipmentModified> {

    /**
     * 提交设备更改信息
     * @param workOrderId
     * @param equipment
     */
    void commitModified(String workOrderId, EquipmentModified equipment);

    /**
     * 通过申请
     * @param modifiedId
     */
    void passModified(String modifiedId);
}
