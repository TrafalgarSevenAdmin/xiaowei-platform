package com.xiaowei.wechat.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.service.IWechatPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private IWechatPayService wechatPayService;

    /**
     * 创建一个订单或创建此订单的签名
     * 对前端来说是获取此订单的签名消息等。
     * @param order
     * @return
     */
    @GetMapping("/")
    public Result payOrder(String order)  {
        Result result = wechatPayService.createPayOrder(order);
        return result;
    }

    @PostMapping("/back")
    public String payBack(@RequestBody String xmlData) throws WxPayException, ParseException {
        WxPayOrderNotifyResult wxPayOrderNotifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        log.info("收到微信回调的支付完成信息,业务订单号：" + wxPayOrderNotifyResult.getOutTradeNo());
        //由于返回对象是微信，需要返回业务成功标识
        return wechatPayService.payInfoHandler(wxPayOrderNotifyResult,true);
    }



}
