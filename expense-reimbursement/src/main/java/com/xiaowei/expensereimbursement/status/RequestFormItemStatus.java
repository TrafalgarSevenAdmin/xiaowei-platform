package com.xiaowei.expensereimbursement.status;

public enum RequestFormItemStatus {
    /**
     * 草稿
     */
    DRAFT(0),

    /**
     * 正常
     */
    NORMAL(1),

    /**
     * 不予报销
     */
    REFUSE(2);

    Integer status;

    RequestFormItemStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
