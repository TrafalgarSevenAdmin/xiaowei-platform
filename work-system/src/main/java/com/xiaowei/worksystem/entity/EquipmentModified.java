package com.xiaowei.worksystem.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 待审核的设备实体
 */
@Entity
@Table(name = "W_EQUIPMENT_MODIFIED")
public class EquipmentModified extends BaseEntity{
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
     * 提交此审核的工程师
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "engineer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser engineer;

    /**
     * 此申请所属的工单信息
     */
    @ManyToOne(targetEntity = WorkOrder.class)
    @JoinColumn(name = "workOrder_id")
    @Fetch(FetchMode.JOIN)
    private WorkOrder workOrder;



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

    public SysUser getEngineer() {
        return engineer;
    }

    public void setEngineer(SysUser engineer) {
        this.engineer = engineer;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }
}
