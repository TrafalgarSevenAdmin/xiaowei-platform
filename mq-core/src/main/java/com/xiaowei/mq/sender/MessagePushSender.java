package com.xiaowei.mq.sender;

import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MqQueueConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePushSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送微信用户消息
     * @param userMessageBean
     */
    public void sendWxMessage(UserMessageBean userMessageBean) {
        amqpTemplate.convertAndSend(MqQueueConstant.WX_MESSAGE_PUSH_QUEUE, userMessageBean);
    }

}
