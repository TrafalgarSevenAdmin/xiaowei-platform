package com.xiaowei.worksystem.receiver;

import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.pay.consts.PayStatus;
import com.xiaowei.pay.entity.XwOrder;
import com.xiaowei.pay.repository.XwOrderRepository;
import com.xiaowei.pay.status.XwType;
import com.xiaowei.worksystem.service.IWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 订单支付完成
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.ORDER_DEFAULT_PAYED_QUEUE)
public class OrderPayedReceiver {

    @Autowired
    private IWorkOrderService workOrderService;
    @Autowired
    private XwOrderRepository xwOrderRepository;


    /**
     * 订单支付完成
     */
    @RabbitHandler
    @Transactional
    public void messageReceiver(String order) {
        log.info("收到订单" + order + "支付完成通知");
        Optional<XwOrder> optional = xwOrderRepository.findById(order);
        EmptyUtils.assertOptional(optional,"没有查询到支付订单");
        XwOrder xwOrder = optional.get();
        //判断是否支付完成
        if(PayStatus.paid.equals(xwOrder.getStatus())){
            log.info("订单未支付完成:"+order);
            return;
        }
        Integer xwType = xwOrder.getXwType();
        if(XwType.WORKORDER.getStatus().equals(xwType)){//工单支付订单
            workOrderService.payServiceItem(xwOrder.getBusinessId());
        }

    }

}
