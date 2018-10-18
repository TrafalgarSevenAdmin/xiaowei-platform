package com.xiaowei.account.consts;

/**
 * Created by yuanxuan on 2018/3/29.
 */
public enum TenementStatus {

    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 禁用
     */
    FORBIDDEN(1),

    /**
     * 删除
     */
    DELETE(99);

    private Integer status;

    TenementStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
