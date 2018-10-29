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
    MAINTENANCEOFDISPATCHING("qMALx0jyp2x2wt5yGqKn70JB1sJa3qCwPCxjTAF9sjQ"),
    /**
     * 费用审核通知
     */
    EXPENSEAUDITNOTICE("nNMulWT7-2sku-CBiHbtz_cX8IxcpQ6QRVlmQAxRyiU"),
    /**
     * 费用审核结果通知
     */
    NOTIFICATIONOFAUDITRESULTS("VLm12QocXIWtoowNxC6_9KZr9-uSOeU_OhbsyFZjmgs"),
    /**
     * 工单处理通知
     */
    PROCESSINGNOTIFICATION("kheEs_KCgIc9veQvxl_YcjLcwUk2Zd9J4qYs0GJcyqw"),
    /**
     * 需要派单员派单的通知
     */
    MESSAGETOSENDORDER("_qVAauBtZ9kKNLEYd2_gJ_NgPtgfa03sBfstsUtXywE");

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
