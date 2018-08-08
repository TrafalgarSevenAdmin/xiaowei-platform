package com.xiaowei.wechat.receiver;

import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.wechat.service.IWechatPayService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RabbitListener(queues = MqQueueConstant.ORDER_EXPIRED_DELAY_QUEUE)
public class WxPayExpireReceiver {

    @Autowired
    private IWechatPayService wechatPayService;

    @RabbitHandler
    public void messageSend(String order) {
        try {
            wechatPayService.closeOrder(order, "超时自动关闭");
        } catch (Exception e) {
            log.warn(e);
        }
    }
}
