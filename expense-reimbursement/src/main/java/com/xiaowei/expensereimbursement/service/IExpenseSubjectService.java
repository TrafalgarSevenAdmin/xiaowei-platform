package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;


public interface IExpenseSubjectService extends IBaseService<ExpenseSubject> {

    ExpenseSubject saveExpenseSubject(ExpenseSubject expenseSubject);

    ExpenseSubject updateExpenseSubject(ExpenseSubject expenseSubject);
}
