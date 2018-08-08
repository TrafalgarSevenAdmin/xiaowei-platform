package com.xiaowei.worksystem.receiver;

import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.worksystem.service.IServiceItemService;
import com.xiaowei.worksystem.service.IWorkOrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单支付完成
 */
@Log4j2
@Component
@RabbitListener(queues = MqQueueConstant.ORDER_DEFAULT_PAYED_QUEUE)
public class OrderPayedReceiver {


    /**
     * 订单支付完成
     */
    @RabbitHandler
    public void messageReceiver(String order) {
        log.info("收到订单" + order + "支付完成通知");

    }

}
