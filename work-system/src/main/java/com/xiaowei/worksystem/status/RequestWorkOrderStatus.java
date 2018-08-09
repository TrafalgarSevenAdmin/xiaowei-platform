package com.xiaowei.worksystem.status;

public enum RequestWorkOrderStatus {
    /**
     * 未处理
     */
    UNTREATED(0),
    /**
     * 已处理
     */
    PROCESSED(1),
    /**
     * 已取消
     */
    CANCEL(2);

    private Integer status;

    RequestWorkOrderStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
