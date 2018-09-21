package com.xiaowei.wechat.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.xiaowei.core.result.Result;
import com.xiaowei.wechat.dto.PayType;
import com.xiaowei.wechat.service.IWechatPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
     * 对前端来说是获取此订单的签名消息或者此支付二维码（根据参数决定）
     * @param order
     * @param type  支付类型，若是JSAPI，返回签名；若是QR,返回base64类型的二维码
     * @return
     */
    @GetMapping("/")
    public Result payOrder(String order, @RequestParam(value = "type") PayType type) {
        if (type == null) {
            //默认微信支付
            type = PayType.JSAPI;
        }
        Result result = wechatPayService.createPayOrder(order,type);
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
