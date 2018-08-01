package com.xiaowei.mq.bean;

import com.xiaowei.mq.constant.MessageType;
import lombok.Data;

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
    public static class Payload implements Serializable {
        public String data;
        public String color;

        public Payload(String data, String color) {
            this.data = data;
            this.color = color;
        }
    }

}
