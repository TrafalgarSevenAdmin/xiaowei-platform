package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 报销单明细
 */
@Table(name = "E_EXPENSEFORMITEM")
@Entity
@Data
public class ExpenseFormItem extends MultiBaseEntity {
    /**
     * 所属科目编号
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 所属科目内容(json)
     */
    @Lob
    private String subjectContent;
    /**
     * 填报金额
     */
    private Double fillFigure;
    /**
     * 初审金额
     */
    private Double firstFigure;
    /**
     * 复审金额
     */
    private Double secondFigure;
    /**
     * 明细状态
     */
    private Integer status;
    /**
     * 费用说明
     */
    private String state;
    /**
     * 发票照片
     */
    @Lob
    private String invoiceFileStore;
    /**
     * 支付凭证
     */
    @Lob
    private String voucherFileStore;
    /**
     * 所属报销单
     */
    @ManyToOne(targetEntity = ExpenseForm.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseFormId")
    @JsonIgnore
    private ExpenseForm expenseForm;

    /**
     * 单价
     */
    private Double unitPrice;

    /**
     * 数量
     */
    private Integer count;

}
