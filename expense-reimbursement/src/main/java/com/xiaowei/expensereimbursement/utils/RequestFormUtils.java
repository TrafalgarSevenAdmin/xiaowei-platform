package com.xiaowei.expensereimbursement.utils;

import com.xiaowei.expensereimbursement.status.RequestFormStatus;

public class RequestFormUtils {
    public static final Integer[] CANUPDATE = {RequestFormStatus.DRAFT.getStatus(),
            RequestFormStatus.TURNDOWN.getStatus()};
}
