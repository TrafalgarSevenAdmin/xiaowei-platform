package com.xiaowei.mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.xiaowei.mq.constant.MqQueueConstant.*;

@Configuration
public class RabbitMQConfig {

    // 创建微信推送的消息队列
    @Bean
    public Queue wxMessageQueue() {
        return new Queue(WX_MESSAGE_PUSH_QUEUE);
    }

}