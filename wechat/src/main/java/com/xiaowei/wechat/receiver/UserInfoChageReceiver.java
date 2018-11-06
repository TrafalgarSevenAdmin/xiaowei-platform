package com.xiaowei.wechat.receiver;

import com.xiaowei.mq.bean.UserChageMassage;
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
    private IWxUserService wxUserService;

    @RabbitHandler
    public void messageReceiver(UserChageMassage userChageMassage) {
        switch (userChageMassage.getType()) {
            case Bind:
                try {
                    wxUserService.bindUser(userChageMassage.getUserId(),userChageMassage.getOpenId());
                } catch (Exception e) {
                    log.error("绑定用户出错！", e);
                }
                break;
            case Chage:
                try {
                    wxUserService.syncUser(userChageMassage.getUserId());
                } catch (Exception e) {
                    log.error("主动同步用户标签出错！", e);
                }
                break;
            default:
                log.error("类型错误!" + userChageMassage.getType());
        }

    }
}
