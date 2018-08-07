package com.xiaowei.wechat.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.xiaowei.core.result.Result;

public interface IWechatPayService {

    /**
     * 根据订单号状态创建支付订单给微信
     * 若微信订单号已经被创建，就重新拉取签名使前端支付
     * @param order
     * @return
     */
    Result createPayOrder(String order);

    /**
     * 处理返回的结果信息
     * @param wxPayOrderNotifyResult
     * @return
     */
    String payInfoHandler(WxPayOrderNotifyResult wxPayOrderNotifyResult, Boolean ipAddrJudge);

    void closeOrder(String order, String message);
}
