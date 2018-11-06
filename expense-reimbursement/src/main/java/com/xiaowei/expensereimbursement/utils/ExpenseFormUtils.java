package com.xiaowei.expensereimbursement.utils;

import com.xiaowei.expensereimbursement.status.ExpenseFormStatus;
import com.xiaowei.expensereimbursement.status.RequestFormStatus;

public class ExpenseFormUtils {
    public static final Integer[] CANUPDATE = {ExpenseFormStatus.DRAFT.getStatus(),
            ExpenseFormStatus.TURNDOWN.getStatus()};
    public static final Integer[] REQUESTCANUPDATE = {RequestFormStatus.DRAFT.getStatus(),
            RequestFormStatus.TURNDOWN.getStatus()};
}
