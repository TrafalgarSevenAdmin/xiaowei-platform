package com.xiaowei.worksystem.status;

public enum WorkOrderCreatedType {
    /**
     * 用户创建
     */
    PROPOSER(0),

    /**
     * 后台工作人员创建
     */
    BACKGROUNDER(1),

    /**
     * 自动创建
     */
    ENGINEER(2);

    private Integer status;

    WorkOrderCreatedType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
