package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;


public interface IExpenseFormService extends IBaseService<ExpenseForm> {

    ExpenseForm saveExpenseForm(ExpenseForm expenseForm);

    ExpenseForm updateExpenseForm(ExpenseForm expenseForm);
}
