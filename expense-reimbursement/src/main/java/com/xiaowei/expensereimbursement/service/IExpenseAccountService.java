package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.ExpenseAccount;


public interface IExpenseAccountService extends IBaseService<ExpenseAccount> {

    ExpenseAccount saveExpenseAccount(ExpenseAccount expenseAccount);

    ExpenseAccount updateExpenseAccount(ExpenseAccount expenseAccount);

}
