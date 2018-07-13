package com.xiaowei.wechat.receiver;

import com.xiaowei.mq.bean.UserMessageBean;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.java.Log;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Component
@RabbitListener(queues = MqQueueConstant.WX_MESSAGE_PUSH_QUEUE)
public class MessageSendReceiver {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 接受消息队列中的数据并推送给微信用户
     * @param messageBean
     */
    @RabbitHandler
    public void messageSend(UserMessageBean messageBean) {
        String userId = messageBean.getUserId();
        Optional<WxUser> byUserId = wxUserService.findByUserId(userId);
        if (!byUserId.isPresent()) {
            //如果用户没有绑定微信，这个就推送不了
            // TODO: 2018/7/13 0013 错误怎么展示
            log.warning("推送消息给微信用户失败！此用户{" + userId + "}没有绑定微信！消息内容：" + messageBean);
        }
        //通过模板推送消息
        WxMpTemplateMessage template = new WxMpTemplateMessage();
        template.setTemplateId((String) messageBean.getMessageType().getPayload());
        List<WxMpTemplateData> data = new ArrayList<>();
        messageBean.getData().forEach((key,payload)->
                data.add(new WxMpTemplateData(key, payload.data, payload.color))
        );
        template.setData(data);
        template.setToUser(byUserId.get().getOpenId());
        template.setUrl(messageBean.getUrl());
        try {
            //发送消息
            wxMpService.getTemplateMsgService().sendTemplateMsg(template);
        } catch (WxErrorException e) {
            // TODO: 2018/7/13 0013 错误怎么展示
            log.warning("推送消息给微信用户{" + userId + "}失败！消息内容：" + messageBean);
            e.printStackTrace();
        }
    }

}
