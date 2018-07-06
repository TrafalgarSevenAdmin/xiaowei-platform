package com.xiaowei.worksystem.status;

public enum InvOrderInType {
    /**
     * 采购入库
     */
    PURCHASE(1),

    /**
     * 调拨入库
     */
    ALLOCATION(2),

    /**
     * 库存调整盘亏
     */
    INVENTORY_ADJUSTMENT(3),

    /**
     * 设备维修（坏件退回）
     */
    REPAIR(4),

    /**
     * 其它
     */
    OTHER(5);

    private Integer status;

    InvOrderInType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
