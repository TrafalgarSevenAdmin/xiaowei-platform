package com.xiaowei.expensereimbursement.status;

public enum  ExpenseFormStatus {
    /**
     * 草稿
     */
    DRAFT(0),

    /**
     * 待审核
     */
    PREAUDIT(1),

    /**
     * 初审通过
     */
    FIRSTAUDIT(2),

    /**
     * 复审通过
     */
    SECONDAUDIT(3),

    /**
     * 驳回
     */
    TURNDOWN(4),

    /**
     * 删除
     */
    DELETE(99);

    Integer status;

    ExpenseFormStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
