package com.xiaowei.worksystem.receiver;

import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.mq.constant.TaskType;
import com.xiaowei.worksystem.service.IWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工单报销消息
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.WK_EXPENSEING__QUEUE)
public class WorkOrderExpenseReceiver {

    @Autowired
    private IWorkOrderService workOrderService;

    /**
     * 工单报销消息
     */
    @RabbitHandler
    public void messageReceiver(TaskMessage taskMessage) {
        log.info("工单报销消息" + taskMessage.getObjectId() + "通知");

        try {
            final TaskType taskType = taskMessage.getTaskType();
            switch (taskType){
                case TO_EXPENSEING:workOrderService.expenseing(taskMessage.getObjectId());break;
                case FINISHED_EXPENSE:workOrderService.finishedExpense(taskMessage.getObjectId());break;
            }
        } catch (Exception e) {
            log.error("处理工单报销中出错！", e);
        }

    }

}
