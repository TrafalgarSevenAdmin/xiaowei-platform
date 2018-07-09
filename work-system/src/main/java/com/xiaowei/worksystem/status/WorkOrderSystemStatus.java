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
     * 处理中
     */
    INHAND(2),
    /**
     * 处理完成
     */
    COMPLETEINHAND(3),

    /**
     * 待归档
     */
    ARCHIVEING(4),

    /**
     * 归档
     */
    ARCHIVED(5),

    /**
     * 待审批
     */
    APPROVE(6),

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
