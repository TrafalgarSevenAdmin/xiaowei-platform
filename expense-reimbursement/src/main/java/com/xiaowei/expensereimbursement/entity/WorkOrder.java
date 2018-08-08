package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工单实体
 */
@Entity
@Table(name = "W_WORKORDER")
@Data
public class WorkOrder extends BaseEntity {

    /**
     * 工单编号
     */
    @Column(unique = true)
    private String code;


    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 工单类型
     */
    private String workOrderType;

    /**
     * 针对后台处理人员状态
     */
    private Integer systemStatus;
    /**
     * 针对用户状态
     */
    private Integer userStatus;
    /**
     * 创建方式
     */
    private Integer createdType;

}
