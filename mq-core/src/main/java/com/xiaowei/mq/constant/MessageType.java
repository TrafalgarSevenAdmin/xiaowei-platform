package com.xiaowei.mq.constant;

/**
 * 推送消息的类型
 * 在微信中，需要创建消息模板，并使用此消息id来推送消息
 */
public enum  MessageType {

    // ===================================== 微信推送使用 ===================================

    /**
     * 派单通知
     */
    MAINTENANCEOFDISPATCHING("yGkZz0WqHlSL1BjmPwISqfTG7pJPGS23cotuNEGA0vM");

    // ===================================== 微信推送使用 ===================================


    /**
     * 此状态的附加信息
     * 比如这个消息会给微信发送，那么附加状态就应该包括一个工单状态的消息模板id,或者其他
     */
    private Object payload;

    MessageType(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}
