package com.xiaowei.wechat.receiver;

import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.constant.MqQueueConstant;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 延迟任务的处理
 */
@Log
@Component
@RabbitListener(queues = MqQueueConstant.DELAY_TASK_QUEUE)
public class DelayTaskReceiver {

    /**
     * 处理延迟任务
     * @param messageBean
     */
    @RabbitHandler
    public void messageReceiver(TaskMessage messageBean) {
        System.out.println(MessageFormat.format("收到延时消息:{0}，一共延时:{1}", messageBean.taskName, new Date().getTime() - messageBean.getStartTime().getTime()));
    }

}
