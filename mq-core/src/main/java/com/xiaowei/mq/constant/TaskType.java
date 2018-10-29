package com.xiaowei.mq.constant;

import java.io.Serializable;

/**
 * 任务类型
 */
public enum TaskType implements Serializable {

    /**
     * 报销中
     */
    TO_EXPENSEING,

    /**
     * 报销完成
     */
    FINISHED_EXPENSE,

    /**
     * 自动通过审核
     */
    AUTO_PASS_QUALITY_CHACK,
    /**
     * 自动待归档
     */
    AUTO_PREPIGEONHOLE;
    
    
}
