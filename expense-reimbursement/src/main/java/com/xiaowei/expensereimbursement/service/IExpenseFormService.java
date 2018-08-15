package com.xiaowei.expensereimbursement.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;

import java.util.Map;


public interface IExpenseFormService extends IBaseService<ExpenseForm> {

    ExpenseForm saveExpenseForm(ExpenseForm expenseForm);

    ExpenseForm updateExpenseForm(ExpenseForm expenseForm);

    ExpenseForm firstAudit(ExpenseForm expenseForm, Boolean audit);

    ExpenseForm secondAudit(ExpenseForm expenseForm, Boolean audit);

    Map<String,Object> auditCountByUserId(String userId);
}
