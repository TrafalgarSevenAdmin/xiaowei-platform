package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 报销单
 */
@Table(name = "E_EXPENSEFORM")
@Entity
@Data
public class ExpenseForm extends BaseEntity {
    /**
     * 所属工单编号
     */
    private String workOrderCode;
    /**
     * 报销单照片
     */
    private String formFileStore;
    /**
     * 填报总计金额
     */
    private Double fillAmount;
    /**
     * 初审总计金额
     */
    private Double firstTrialAmount;
    /**
     * 复审总计金额
     */
    private Double secondTrialAmount;
    /**
     * 初审人
     */
    private List<SysUser> firstTrials;
    /**
     * 复审人
     */
    private List<SysUser> secondTrials;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 初审审批意见
     */
    private String firstOption;
    /**
     * 复审审批意见
     */
    private String secondOption;
}
