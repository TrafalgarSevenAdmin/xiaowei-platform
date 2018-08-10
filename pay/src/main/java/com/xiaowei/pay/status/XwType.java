package com.xiaowei.pay.status;

public enum XwType {
    /**
     * 工单支付订单
     */
    WORKORDER(0);

    Integer status;

    XwType(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
