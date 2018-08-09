package com.xiaowei.account.consts;

public enum RoleType {
    /**
     * 公司角色
     */
    COMPANYROLE(0),

    /**
     * 托管角色
     */
    TRUSTEESHIPROLE(1);

    private Integer status;

    RoleType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
