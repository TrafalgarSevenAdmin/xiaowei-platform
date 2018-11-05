package com.xiaowei.expensereimbursement.status;

/**
 * 小组审核类型
 */
public enum AuditExpenseType {
    /**
     * 报销初审人
     */
    FIRSTEXPENSE,
    /**
     * 报销复审人
     */
    SECONDEXPENSE,
    /**
     * 费用申请审核人
     */
    REQUEST;
}
