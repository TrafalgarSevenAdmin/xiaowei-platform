package com.xiaowei.worksystem.status;

public enum WorkFlowStatus {

    /**
     * 范围内
     */
    NORMAL(0),
    /**
     * 范围外
     */
    FORBIDDEN(1);

    private Integer status;

    WorkFlowStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
