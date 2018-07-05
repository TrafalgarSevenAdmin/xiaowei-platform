package com.xiaowei.worksystem.status;

public enum WorkpieceStoreType {
    /**
     * 个人厂库
     */
    USER(0),

    /**
     * 总库（核心厂库）
     */
    CORE(1);

    private Integer status;

    WorkpieceStoreType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
