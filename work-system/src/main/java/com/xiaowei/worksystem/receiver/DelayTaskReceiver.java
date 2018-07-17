package com.xiaowei.worksystem.receiver;

import com.xiaowei.mq.bean.TaskMessage;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.worksystem.service.IServiceItemService;
import com.xiaowei.worksystem.service.IWorkOrderService;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * 延迟任务队列的接收器
 */
@Log
@Component
@RabbitListener(queues = MqQueueConstant.DELAY_TASK_QUEUE)
public class DelayTaskReceiver {

    @Autowired
    private IWorkOrderService workOrderService;
    @Autowired
    private IServiceItemService serviceItemService;

    /**
     * 处理延迟任务
     * @param messageBean
     */
    @RabbitHandler
    public void messageReceiver(TaskMessage messageBean) {
        log.log(Level.INFO,MessageFormat.format("收到延时消息，一共延时:{0}s",  (new Date().getTime() - messageBean.getStartTime().getTime()/1000)));

        switch (messageBean.getTaskType()) {
            case AUTO_PREPIGEONHOLE:
                try {
                    workOrderService.workOrderToPrepigeonhole(messageBean.getObjectId());
                } catch (Exception e) {
                    log.warning("延时任务处理过程中错误："+e.getMessage());
                    e.printStackTrace();
                }
                break;
            case AUTO_PASS_QUALITY_CHACK:
                try {
                    //定时通过质检
                    serviceItemService.qualityServiceItem(messageBean.getObjectId(),true);
                } catch (Exception e) {
                    log.warning("自动通过质检过程中错误："+e.getMessage());
                    e.printStackTrace();
                }
                break;
        }


    }

}
