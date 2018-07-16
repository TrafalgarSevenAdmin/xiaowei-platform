package com.xiaowei.mq.constant;

import java.io.Serializable;

/**
 * 任务类型
 */
public enum TaskType implements Serializable {

    /**
     * 自动转成待归档
     */
    AUTO_PREPIGEONHOLE,

    /**
     * 自动通过审核
     */
    AUTO_PASS_QUALITY_CHACK;
    
    
}
