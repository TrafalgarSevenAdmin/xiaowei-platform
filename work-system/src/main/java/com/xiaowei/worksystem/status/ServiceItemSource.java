package com.xiaowei.worksystem.status;

public enum ServiceItemSource {

    /**
     * 后台工作人员创建
     */
    BACKGROUNDER(1),

    /**
     * 工程师创建
     */
    ENGINEER(2);

    private Integer status;

    ServiceItemSource(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
