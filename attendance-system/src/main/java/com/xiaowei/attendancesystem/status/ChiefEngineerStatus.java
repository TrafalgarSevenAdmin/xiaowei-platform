package com.xiaowei.attendancesystem.status;

public enum ChiefEngineerStatus {
    /**
     * 正常
     */
    NORMAL(0),
    /**
     * 禁用
     */
    FORBIDDEN(1),

    /**
     * 删除
     */
    DELETE(99);

    private Integer status;

    ChiefEngineerStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
