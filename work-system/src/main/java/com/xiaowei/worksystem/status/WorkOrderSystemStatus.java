package com.xiaowei.worksystem.status;

public enum WorkOrderSystemStatus {
    /**
     * 待指派
     */
    ASSIGNED(0),

    /**
     * 待审批
     */
    APPROVED(1),

    /**
     * 待归档
     */
    ARCHIVEING(2),

    /**
     * 归档
     */
    ARCHIVED(3),

    /**
     * 待审批
     */
    INHAND(4),

    /**
     * 删除
     */
    DELETE(99);

    private Integer status;

    WorkOrderSystemStatus(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
