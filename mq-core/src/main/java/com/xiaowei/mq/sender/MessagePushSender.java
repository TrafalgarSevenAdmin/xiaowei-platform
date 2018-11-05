package com.xiaowei.mq.sender;

import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.bean.UserChageMassage;
import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MqQueueConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xiaowei.mq.constant.MqQueueConstant.DELAY_EXCHAGE;
import static com.xiaowei.mq.constant.MqQueueConstant.DELAY_TASK_ROUTING;

@Service
public class MessagePushSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送微信用户消息
     *
     * @param userMessageBean
     */
    public void sendWxMessage(UserMessageBean userMessageBean) {
        amqpTemplate.convertAndSend(MqQueueConstant.WX_MESSAGE_PUSH_QUEUE, userMessageBean);
    }

    /**
     * 发送工单报销中的消息
     */
    public void sendWorkOrderExpenseingMessage(TaskMessage taskMessage) {
        amqpTemplate.convertAndSend(MqQueueConstant.WK_EXPENSEING__QUEUE, taskMessage);
    }

    /**
     * 发送用户信息更改的消息
     */
    public void sendUserInfoChageMessage(UserChageMassage userChageMassage) {
        amqpTemplate.convertAndSend(MqQueueConstant.USER_INFO_CHANGE_QUEUE, userChageMassage);
    }

    /**
     * 用户微信绑定消息
     */
    public void sendWxUserBind(String openId,String userId) {
        amqpTemplate.convertAndSend(MqQueueConstant.USER_INFO_CHANGE_QUEUE, userId);
    }


    /**
     * 发送订单支付完成的消息
     *
     * @param orderId
     */
    public void sendOrderPayedMessage(String orderId) {
        this.sendOrderPayedMessage(MqQueueConstant.ORDER_DEFAULT_PAYED_QUEUE, orderId);
    }

    public void sendOrderPayedMessage(String queue, String orderId) {
        amqpTemplate.convertAndSend(queue, orderId);
    }

    /**
     * 发送延迟任务
     *
     * @param messageBean 消息
     * @param delay       延时毫秒数
     * @see com.xiaowei.mq.constant.MqQueueConstant
     */
    public void sendDelayTask(TaskMessage messageBean, int delay) {
        this.sendDelay(DELAY_TASK_ROUTING, messageBean, delay);
    }

    /**
     * 延迟任务
     *
     * @param routingKey  指定路由规则
     * @param messageBean 消息
     * @param delay       延时毫秒数
     */
    public void sendDelay(String routingKey, Object messageBean, int delay) {
        amqpTemplate.convertAndSend(DELAY_EXCHAGE, routingKey, messageBean, message -> {
            message.getMessageProperties().setDelay(delay);
            return message;
        });
    }

}
