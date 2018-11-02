package com.xiaowei.worksystem.entity;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * 设备实体
 */
@Data
@Entity
@Table(name = "W_EQUIPMENT")
@SQLDelete(sql = "update w_equipment set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Equipment extends MultiBaseEntity {
    /**
     * 设备名称
     */
    String equipmentName;

    /**
     * 设备编号
     */
    String equipmentNo;

    /**
     * 设备地址
     */
    String address;

    /**
     * 类型
     */
    String equipmentType;

    /**
     * 品牌
     */
    String equipmentBrand;

    /**
     * 型号
     */
    String equipmentModel;

    /**
     * 生产日期
     */
    Date madeDate;

    /**
     * 生产批次
     */
    String batchNo;

    /**
     * 安装日期
     */
    Date installDate;

    /**
     * 维保状态
     */
    String warrantyStatus;

    /**
     * 服务合同号
     */
    String contractNo;

    /**
     * 网点名称
     */
    String branchName;

    /**
     * 客户设备编号
     */
    String customerEquipmentNo;

    /**
     * 安装方式 大堂、穿墙
     */
    String installType;

    /**
     * IP地址
     */
    String ip;

    /**
     * 经度
     */
    Double longitude;

    /**
     * 纬度
     */
    Double latitude;

    /**
     * 维保到期日期
     */
    String warrantyTime;

    /**
     * 安装位置(离行、在行)
     */
    String locationType;

    /**
     * 备注信息
     */
    String note;

    /**
     * 机芯类型
     */
    String coreType;

    /**
     * 状态
     */
    String status;

    @ManyToOne(targetEntity = WorkFlow.class)
    @JoinColumn(name = "workflow_id")
    @Fetch(FetchMode.JOIN)
    WorkFlow workFlow;

//    /**
//     * 操作系统名称
//     */
//    String osName;
//
//    /**
//     * 操作系统版本
//     */
//    String osVersion;
//
//    /**
//     * ATMC名称
//     */
//    String atmcName;
//
//    /**
//     * ATMC版本
//     */
//    String atmcVersion;
//
//    /**
//     * SP名称
//     */
//    String spName;
//
//    /**
//     * SP版本
//     */
//    String spVersion;

    /**
     * 创建人
     */
    String createUser;


    /**
     * 所属服务对象
     */
    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    @Fetch(FetchMode.JOIN)
    Customer customer;

    @PrePersist
    public void onCreateSetUser() {
        createUser = LoginUserUtils.getLoginUser().getNickName();
    }
}
