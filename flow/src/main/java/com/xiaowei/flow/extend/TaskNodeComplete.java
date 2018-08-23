package com.xiaowei.flow.extend;

import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;

/**
 * 任务节点完成处理
 * 主要用于业务处理。
 */
public interface TaskNodeComplete {

    /**
     * 任务执行
     * @param parameter
     * @return
     */
    TaskCompleteExtendResult execute(TaskCompleteExtendParameter parameter);

    /**
     * 节点执行完毕。在此才可以操作
     * @param parameter
     */
    void complete(TaskCompleteExtendParameter parameter);
}
