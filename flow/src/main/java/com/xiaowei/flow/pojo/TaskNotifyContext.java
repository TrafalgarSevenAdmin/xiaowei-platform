package com.xiaowei.flow.pojo;

import com.xiaowei.flow.constants.TaskNotifyPersonType;
import com.xiaowei.flow.entity.FlowTask;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * 任务通知内容
 */
@Data
@Builder
public class TaskNotifyContext {

    /**
     * 任务
     */
    FlowTask task;


    /**
     * 通知人类型
     */
    TaskNotifyPersonType notifyPersonType;

    /**
     * 通知的用户集合
     */
    Set<String> userIds;

    /**
     * 通知的角色集合
     */
    Set<String> roleIds;

    /**
     * 通知的部门集合
     */
    Set<String> departmentIds;
}
