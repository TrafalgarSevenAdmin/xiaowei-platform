package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 二级费用科目实体
 */
@Table(name = "E_EXPENSEACCOUNT")
@Entity
@Data
public class ExpenseAccount extends BaseEntity {
    /**
     * 科目名称
     */
    private String accountName;

    /**
     * 科目内容
     */
    private String accountContent;

    /**
     * 科目说明
     */
    private String state;

    /**
     * 税率
     */
    private Float taxRate;

    /**
     * 所属一级科目
     */
    @ManyToOne(targetEntity = ExpenseSubject.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseSubjectId")
    @JsonIgnore
    private ExpenseSubject expenseSubject;

}
