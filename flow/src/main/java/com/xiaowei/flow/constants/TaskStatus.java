package com.xiaowei.flow.constants;

/**
 * 任务状态
 */
public enum TaskStatus {
    /**
     * 被创建
     */
    Created,

    /**
     * 运行中
     */
    Running,

    /**
     * 任务完成
     */
    Finished,

    /**
     * 任务被关闭
     */
    Close,

    //若有子流程情况的、就会有子任务，任务状态就会扩充挂起等类型
}
