package com.xiaowei.worksystem.status;

public enum WarehouseType {
    /**
     * 备件库
     */
    SPARE(1),

    /**
     * 个人库
     */
    PERSONAL(2);

    private Integer status;

    WarehouseType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
