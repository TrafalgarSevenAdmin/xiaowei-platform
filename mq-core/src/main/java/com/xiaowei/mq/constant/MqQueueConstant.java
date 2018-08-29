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
     * 发送工单报销中的消息
     */
    public static final String WK_EXPENSEING__QUEUE = "wk_expenseing__queue";

    /**
     * 订单支付完成的消息
     * 默认使用此队列通知，若有其他的支付入口发生，可以通过此更改回调通知地址
     */
    public static final String ORDER_DEFAULT_PAYED_QUEUE = "order_default_payed_queue";

    /**
     * 订单支付后处理队列，用于二次校验微信支付数据以及定时关闭订单
     */
    public static final String ORDER_EXPIRED_DELAY_QUEUE = "order_expired_delay_queue";

    /**
     * 用于延迟的任务队列
     */
    public static final String DELAY_TASK_QUEUE = "delay_task_queue";

    /**
     * 延时任务,
     */
    public static final String DELAY_TASK_ROUTING = "delay.task";

    /**
     * 支付超时任务
     */
    public static final String DELAY_PAY_TASK_ROUTING = "delay.pay.task";

    /**
     * 延时消息
     */
    public static final String DELAY_MESSAGE_ROUTING = "delay.message";

}
