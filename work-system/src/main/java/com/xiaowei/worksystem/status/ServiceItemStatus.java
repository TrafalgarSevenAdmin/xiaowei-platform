package com.xiaowei.worksystem.status;

public enum ServiceItemStatus {
    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 待确认
     */
    CONFIRMED(1),

    /**
     * 确认不执行
     */
    INEXECUTION(2),

    /**
     * 待付款
     */
    PAIED(3),

    /**
     * 待审核
     */
    AUDITED(4),

    /**
     * 完成
     */
    COMPLETED(5);

    private Integer status;

    ServiceItemStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
