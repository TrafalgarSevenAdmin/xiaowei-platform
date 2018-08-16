package com.xiaowei.expensereimbursement.status;

public enum CostType {
    /**
     * 按公里计费
     */
    KILOMETRE(0),
    /**
     * 按数量计费
     */
    NUMBER(1);

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
