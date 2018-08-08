package com.xiaowei.expensereimbursement.utils;

import com.xiaowei.expensereimbursement.status.ExpenseFormStatus;

public class ExpenseFormUtils {
    public static final Integer[] CANUPDATE = {ExpenseFormStatus.DRAFT.getStatus(),
            ExpenseFormStatus.TURNDOWN.getStatus()};
}
