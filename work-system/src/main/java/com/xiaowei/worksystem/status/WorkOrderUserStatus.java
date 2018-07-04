package com.xiaowei.worksystem.status;

public enum WorkOrderUserStatus {
    /**
     * 待指派
     */
    ASSIGNED(0),

    /**
     * 待接单
     */
    RECEIVED(1),

    /**
     * 待确认
     */
    CONFIRMED(2),

    /**
     * 待付款
     */
    PAIED(3),

    /**
     * 待评价
     */
    EVALUATED(4),

    /**
     * 处理中
     */
    INHAND(5),

    /**
     * 完成
     */
    COMPLETED(6);

    private Integer status;

    WorkOrderUserStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
