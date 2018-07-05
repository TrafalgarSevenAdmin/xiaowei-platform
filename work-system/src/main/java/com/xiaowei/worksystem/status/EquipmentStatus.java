package com.xiaowei.worksystem.status;

public enum EquipmentStatus {
    /**
     * 存在
     */
    LIVE(0),
    /**
     * 删除
     */
    DELETE(99);

    private Integer status;

    EquipmentStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
