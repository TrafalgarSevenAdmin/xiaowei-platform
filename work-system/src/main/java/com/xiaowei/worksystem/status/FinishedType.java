package com.xiaowei.worksystem.status;

public enum FinishedType {
    /**
     * 外部工单正常完成
     */
    OUT_NORMAL_FINISHED(0),
    /**
     * 外部工单终止完成
     */
    OUT_TERMINATION_FINISHED(1),
    /**
     * 内部工单正常完成
     */
    IN_NORMAL_FINISHED(2);

    private Integer status;

    FinishedType(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
