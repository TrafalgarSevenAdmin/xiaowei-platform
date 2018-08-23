package com.xiaowei.flow.pojo;

import com.xiaowei.flow.entity.FlowDefinition;
import com.xiaowei.flow.entity.FlowTask;
import com.xiaowei.flow.entity.FlowTaskExecuteHistory;
import lombok.Builder;
import lombok.Data;

/**
 * 任务节点完成时的扩展执行参数
 */
@Data
@Builder
public class TaskCompleteExtendParameter {
    /**
     * 执行流程
     */
    FlowDefinition flowDefinition;

    /**
     * 执行的任务
     * 对其修改会使数据库更新，最好不要修改
     * 可编辑其中的ext扩展属性
     */
    FlowTask task;

    /**
     * 上一个执行历史
     */
    FlowTaskExecuteHistory lastHistory;

}
