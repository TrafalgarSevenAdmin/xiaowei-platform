package com.xiaowei.worksystem.receiver;

import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.worksystem.service.IWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工单报销中
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.WK_EXPENSEING__QUEUE)
public class WorkOrderExpenseingReceiver {

    @Autowired
    private IWorkOrderService workOrderService;

    /**
     *
     */
    @RabbitHandler
    public void messageReceiver(String workOrderCode) {
        log.info("工单报销中" + workOrderCode + "通知");
        try {
            workOrderService.expenseing(workOrderCode);
        } catch (Exception e) {
            log.error("处理工单报销中出错！",e);
        }

    }

}
