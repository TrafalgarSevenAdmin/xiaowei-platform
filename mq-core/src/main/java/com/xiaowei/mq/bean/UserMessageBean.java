package com.xiaowei.mq.bean;

import com.xiaowei.mq.constant.MessageType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.Serializable;
import java.util.Map;

/**
 * 推送给用户的消息模板
 */
@Data
public class UserMessageBean implements MessageBean {
    String userId;

    /**
     * 推送的消息类型
     */
     MessageType messageType;

    /**
     * 消息获取的网页
     */
    String url;

    /**
     * 推送的数据
     */
    Map<String,Payload> data;

    @Data
    public class Payload{
        public String data;
        public String color;
    }

}
