package com.xiaowei.worksystem.status;

public enum ServiceItemStatus {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 待确认
     */
    CONFIRMED(1),

    /**
     * 待付款
     */
    PAIED(2),

    /**
     * 完成
     */
    COMPLETED(3);

    private Integer status;

    ServiceItemStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
