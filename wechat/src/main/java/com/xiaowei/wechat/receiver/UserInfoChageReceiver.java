package com.xiaowei.wechat.receiver;

import com.xiaowei.account.service.ISysUserService;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户信息改变
 * 需要重新同步到微信中
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.USER_INFO_CHANGE_QUEUE)
public class UserInfoChageReceiver {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IWxUserService wxUserService;

    @RabbitHandler
    public void messageReceiver(String userId) {
        try {
            wxUserService.syncUser(userId);
        } catch (Exception e) {
            log.error("主动同步用户标签出错！",e);
        }
    }
}
