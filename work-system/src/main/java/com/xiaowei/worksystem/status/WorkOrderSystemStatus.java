package com.xiaowei.worksystem.status;

public enum WorkOrderSystemStatus {
    /**
     * 待派发
     */
    DISTRIBUTE(0),
    /**
     * 待接单
     */
    RECEIVE(1),
    /**
     * 预约中
     */
    APPOINTING(2),
    /**
     * 待出发
     */
    DEPART(3),
    /**
     * 行程中
     */
    TRIPING(4),
    /**
     * 处理中
     */
    INHAND(5),
    /**
     * 质检中
     */
    QUALITY(6),
    /**
     * 处理完成
     */
    FINISHHAND(7),
    /**
     * 报销中
     */
    EXPENSEING(8),
    /**
     * 待归档
     */
    PREPIGEONHOLE(9),
    /**
     * 归档
     */
    PIGEONHOLED(10),
    /**
     * 删除
     */
    DELETE(11);

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
