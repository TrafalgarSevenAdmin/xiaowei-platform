package com.xiaowei.worksystem.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 报销账单
 */
@Data
@Entity
@Table(name = "W_EXPENSEACCOUNT")
public class ExpenseAccount extends BaseEntity {
    /**
     * 费用类型
     */
    private String expenseType;
    /**
     * 费用说明
     */
    private String expenseState;
    /**
     * 原因说明
     */
    private String reasonState;
    /**
     * 金额
     */
    private Double money;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 所属工单
     */
    @ManyToOne(targetEntity = WorkOrder.class)
    @JoinColumn(name = "workOrder_id")
    @Fetch(FetchMode.JOIN)
    private WorkOrder workOrder;
    /**
     * 申请人
     */
    @ManyToOne(targetEntity = WorkOrder.class)
    @JoinColumn(name = "proposer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser proposer;
    /**
     * 审核人
     */
    @ManyToOne(targetEntity = WorkOrder.class)
    @JoinColumn(name = "verifier_id")
    @Fetch(FetchMode.JOIN)
    private SysUser verifier;
}
