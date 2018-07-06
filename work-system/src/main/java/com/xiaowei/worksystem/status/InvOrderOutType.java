package com.xiaowei.worksystem.status;

public enum InvOrderOutType {
    /**
     * 销售出库
     */
    SALE(1),

    /**
     * 调拨出库
     */
    ALLOCATION(2),

    /**
     * 库存调整盘亏
     */
    INVENTORY_ADJUSTMENT(3),

    /**
     * 备件维修
     */
    REPAIR(4),

    /**
     * 其它
     */
    OTHER(5);

    private Integer status;

    InvOrderOutType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
