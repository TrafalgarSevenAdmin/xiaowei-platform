package com.xiaowei.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xiaowei.mq.constant.MqQueueConstant.*;

@Configuration
public class RabbitMQConfig {

    // 创建微信推送的消息队列，直接推送到
    @Bean
    public Queue wxMessageQueue() {
        return new Queue(WX_MESSAGE_PUSH_QUEUE);
    }

    //支付成功的默认回调
    @Bean
    public Queue orderDefaultPayedQueue() {
        return new Queue(ORDER_DEFAULT_PAYED_QUEUE);
    }

    //支付超时或二次校验
    @Bean
    public Queue orderExpiredQueue() {
        return new Queue(ORDER_EXPIRED_DELAY_QUEUE);
    }

    // 创建延迟推送的消息队列
    @Bean
    public Queue delayTaskMessageQueue() {
        //持久化
        return new Queue(DELAY_TASK_QUEUE,true);
    }

    // 创建推送的消息队列
    @Bean
    public Queue wkExpenseingQueue() {
        //持久化
        return new Queue(WK_EXPENSEING__QUEUE,true);
    }

    /**
     * 延时交换机，用于分发延时任务
     * @return
     */
    @Bean
    public TopicExchange delayExchange() {
        TopicExchange topicExchange = new TopicExchange(DELAY_EXCHAGE, true, false);
        topicExchange.setDelayed(true);//只需简单一步开启延时消息
        return topicExchange;
    }

    /**
     * 绑定任务消息队列到延时交换机
     * @return
     */
    @Bean
    public Binding bindingDelayTask(Queue delayTaskMessageQueue, TopicExchange delayExchange) {
        return BindingBuilder.bind(delayTaskMessageQueue).to(delayExchange).with(DELAY_TASK_ROUTING);
    }

    @Bean
    public Binding bindingOrderExpiredQueueDelayTask(Queue orderExpiredQueue, TopicExchange delayExchange) {
        return BindingBuilder.bind(orderExpiredQueue).to(delayExchange).with(DELAY_PAY_TASK_ROUTING);
    }

}