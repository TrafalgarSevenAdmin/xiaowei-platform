package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.status.CommonStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备实体
 */
@Entity
@Table(name = "W_EQUIPMENT")
public class Equipment extends BaseEntity{
    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 设备地址
     */
    private String address;

    /**
     * 数据状态
     */
    private Integer status = CommonStatus.LIVE.getStatus();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
