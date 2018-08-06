package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * 一级费用科目实体
 */
@Table(name = "E_EXPENSESUBJECT")
@Entity
@Data
public class ExpenseSubject extends BaseEntity {
    private String subjectName;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "expenseSubject")
    @JsonIgnore
    private List<ExpenseAccount> expenseAccounts;
}
