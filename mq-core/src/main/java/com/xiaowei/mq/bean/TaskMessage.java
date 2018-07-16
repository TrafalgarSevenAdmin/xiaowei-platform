package com.xiaowei.mq.bean;

import lombok.Data;

import java.util.Date;

/**
 * 任务消息
 */
@Data
public class TaskMessage implements MessageBean {
    public String taskName;
    public Date startTime = new Date();
}
