package com.xiaowei.worksystem.status;

public enum WorkOrderEngineerStatus {
    /**
     * 待接单
     */
    RECEIVED(0),

    /**
     * 预约中
     */
    APPOINTING(1),

    /**
     * 待出发
     */
    DEPARTED(2),

    /**
     * 行程中
     */
    TRIPING(3),

    /**
     * 处理中
     */
    INHAND(4),

    /**
     * 处理完成
     */
    COMPLETEINHAND(5),

    /**
     * 待报销
     */
    REIMBURSEMENT(6),

    /**
     * 完成
     */
    COMPLETED(7);

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
