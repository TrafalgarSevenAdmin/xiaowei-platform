package com.xiaowei.flow.constants;

/**
 * 任务执行动作类型
 */
public enum TaskActionType {
    /**
     * 正常动作
     */
    NORMAL,

    /**
     * 异常动作
     * 指流程运行方式与指定不匹配
     * 通常发生在驳回操作中。
     */
    ABNORMAL,

    /**
     * 任务完成动作
     */
    Finished,

    /**
     * 关闭动作
     * 发生在关闭流程时
     */
    CLOSE,
}
