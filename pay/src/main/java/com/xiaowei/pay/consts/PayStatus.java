package com.xiaowei.pay.consts;

/**
 * 支付状态
 */
public enum PayStatus {
    /**
     * 本地订单已创建,但未提交给微信
     */
    created,

    /**
     * 本地订单已经提交给微信，但未付款
     */
    unpaid,

    /**
     * 已付款
     */
    paid,

    /**
     * 订单关闭,未支付
     * 原因如果有，见message
     */
    close,

    /**
     * 付款异常
     * 由于校验时发生异常，不排除黑客攻击等原因造成
     * 详情见message
     */
    abnormal
}
