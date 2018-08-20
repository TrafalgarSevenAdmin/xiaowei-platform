package com.xiaowei.expensereimbursement.status;

public enum RequestFormStatus {
    /**
     * 草稿
     */
    DRAFT(0),

    /**
     * 待审核
     */
    PREAUDIT(1),

    /**
     * 审核通过
     */
    AUDIT(2),


    /**
     * 驳回
     */
    TURNDOWN(3),

    /**
     * 删除
     */
    DELETE(99);

    Integer status;

    RequestFormStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
