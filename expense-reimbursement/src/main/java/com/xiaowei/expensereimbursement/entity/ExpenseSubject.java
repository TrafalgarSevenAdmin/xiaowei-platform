package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 费用科目实体
 */
@Table(name = "E_EXPENSESUBJECT")
@Entity
@Data
public class ExpenseSubject extends BaseEntity {
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 父级科目id
     */
    private String parentId;
    /**
     * 科目代码
     */
    @Column(updatable = false)
    private Integer ownCode;
    /**
     * 科目编号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 科目等级
     */
    private Integer level;

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

}
