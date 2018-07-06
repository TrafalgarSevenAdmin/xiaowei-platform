package com.xiaowei.worksystem.status;

public enum ProductTagType {
    /**
     * 好件
     */
    FINE(1),

    /**
     * 坏件
     */
    BAD(2);

    private Integer status;

    ProductTagType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
