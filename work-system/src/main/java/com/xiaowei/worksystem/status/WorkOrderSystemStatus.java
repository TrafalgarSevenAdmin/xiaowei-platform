package com.xiaowei.worksystem.status;

public enum WorkOrderSystemStatus {
    /**
     * 删除
     */
    DELETE(99);

    private Integer status;

    WorkOrderSystemStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
