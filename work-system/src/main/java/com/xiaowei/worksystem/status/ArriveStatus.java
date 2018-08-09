package com.xiaowei.worksystem.status;

public enum ArriveStatus {

    /**
     * 范围内
     */
    INSIDE(0),
    /**
     * 范围外
     */
    OUTSIDE(1);

    private Integer status;

    ArriveStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
