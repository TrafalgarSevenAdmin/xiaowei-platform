package com.xiaowei.expensereimbursement.status;

public enum CostType {
    /**
     * 按公里计费
     */
    KILOMETRE(0),
    /**
     * 按天数计费
     */
    QUALITYORDER(1),
    /**
     * 按数量计费
     */
    NUMBER(2),
    /**
     * 按金额计费
     */
    COST(3);

    Integer status;

    CostType(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
