package com.xiaowei.worksystem.status;

public enum WorkOrderUserStatus {
    /**
     * 正常
     */
    NORMAO(0),
    /**
     * 待确认
     */
    AFFIRM(1),
    /**
     * 待付款
     */
    PAIED(2),
    /**
     * 待评价
     */
    EVALUATED(3);

    private Integer status;

    WorkOrderUserStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
