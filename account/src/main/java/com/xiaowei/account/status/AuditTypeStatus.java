package com.xiaowei.account.status;

public enum AuditTypeStatus {

    /**
     * 派单
     */
    SENDORDER(0),
    /**
     * 质检工单
     */
    QUALITYORDER(1),
    /**
     * 审核工单
     */
    AUDITORDER(2),
    /**
     * 报销单初审
     */
    FIRSTEXPENSEFORM(3),
    /**
     * 报销单复审
     */
    SECONDEXPENSEFORM(4),
    /**
     * 申请单审核
     */
    APPLICATIONFORM(5);


    Integer status;

    AuditTypeStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
