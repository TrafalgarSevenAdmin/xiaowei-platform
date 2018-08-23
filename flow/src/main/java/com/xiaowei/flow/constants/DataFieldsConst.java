package com.xiaowei.flow.constants;

import java.util.Arrays;
import java.util.List;

public class DataFieldsConst {
    /**
     * 任务数据展示过滤
     */
    public static final List<String> taskViewFilters = Arrays.asList("nextNode.nextNodes","nextNode.flow","nextNode.auth",
            "nowTaskHistory.lastTask.lastTask","nowTaskHistory.lastTask.task","nowTaskHistory.lastTask.node","nowTaskHistory.task","nowTaskHistory.node.auth","nowTaskHistory.node.flow","nowTaskHistory.node.nextNodes",
            "node.auth", "flow.start","flow.viewer");

    /**
     * 任务执行历史展示过滤
     */
    public static final List<String> taskExecuteHistoryViewFilters = Arrays.asList("lastTask",
            "node.nextNodes", "node.auth", "node.flow",
            "task.flow.start", "task.flow.viewer", "task.nextNode.flow", "task.nextNode.auth", "task.nextNode.nextNodes",
            "task.nowTaskHistory.lastTask.lastTask", "task.nowTaskHistory.lastTask.task", "task.nowTaskHistory.lastTask.node",
            "task.nowTaskHistory.task", "task.nowTaskHistory.node.auth", "task.nowTaskHistory.node.flow", "task.nowTaskHistory.node.nextNodes");

    /**
     * 流程节点展示过滤
     */
    public static final List<String> nodeViewFilters = Arrays.asList("lastTask", "flow","nextNodes");
}
