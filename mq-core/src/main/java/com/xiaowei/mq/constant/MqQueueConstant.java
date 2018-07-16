package com.xiaowei.mq.constant;

public class MqQueueConstant {

    /**
     * 延迟交换机，用于推送延时消息
     */
    public static final String DELAY_EXCHAGE = "delay_exchage";

    /**
     * 微信消息推送队列
     */
    public static final String WX_MESSAGE_PUSH_QUEUE = "wx_message_push_queue";

    /**
     * 用于延迟的任务队列
     */
    public static final String DELAY_TASK_QUEUE = "delay_task_queue";

    /**
     * 延时任务
     */
    public static final String DELAY_TASK_ROUTING = "delay.task";

    /**
     * 延时消息
     */
    public static final String DELAY_MESSAGE_ROUTING = "delay.message";

}
