package com.xiaowei.flow.extend;

import com.xiaowei.flow.pojo.TaskCompleteExtendParameter;
import com.xiaowei.flow.pojo.TaskCompleteExtendResult;

/**
 * 任务完成处理
 * 主要用于业务处理。
 */
public interface TaskComplete {

    void complete(TaskCompleteExtendParameter parameter);

}
