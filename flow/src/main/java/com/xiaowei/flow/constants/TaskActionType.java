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
     * 若不指定下一个节点，默认作为回退到上一个节点
     */
    ABNORMAL,

    /**
     * 流程转发操作
     */
    FORWARD,

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
