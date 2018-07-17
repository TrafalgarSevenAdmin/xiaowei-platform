package com.xiaowei.mq.bean;

import com.xiaowei.mq.constant.TaskType;
import lombok.Data;

import java.util.Date;

/**
 * 任务消息
 */
@Data
public class TaskMessage implements MessageBean {

    /**
     * 目标id
     */
    private String objectId;

    /**
     * 此消息的创建时间
     */
    private Date startTime = new Date();

    /**
     * 任务类型
     */
    private TaskType taskType;

    public TaskMessage(String objectId, TaskType taskType) {
        this.objectId = objectId;
        this.taskType = taskType;
    }
}
