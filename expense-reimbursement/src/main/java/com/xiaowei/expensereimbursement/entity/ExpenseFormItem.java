package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 二级费用科目实体
 */
@Table(name = "E_EXPENSEFORMITEM")
@Entity
@Data
public class ExpenseFormItem extends BaseEntity {

}
