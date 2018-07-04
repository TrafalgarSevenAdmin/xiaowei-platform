package com.xiaowei.worksystem.status;

public enum WorkOrderEngineerStatus {
    /**
     * 待接单
     */
    RECEIVED(0),

    /**
     * 待出发
     */
    DEPARTED(1),

    /**
     * 行程中
     */
    TRIPING(2),

    /**
     * 处理中
     */
    INHAND(3),

    /**
     * 处理完成
     */
    COMPLETEINHAND(4),

    /**
     * 待报销
     */
    REIMBURSEMENT(5),

    /**
     * 完成
     */
    COMPLETED(6);

    private Integer status;

    WorkOrderEngineerStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
