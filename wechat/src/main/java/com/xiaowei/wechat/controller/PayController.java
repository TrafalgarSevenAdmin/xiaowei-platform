package com.xiaowei.wechat.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.pay.consts.PayStatus;
import com.xiaowei.pay.entity.XwOrder;
import com.xiaowei.pay.entity.XwOrderResult;
import com.xiaowei.pay.service.IOrderService;
import com.xiaowei.wechat.consts.MagicValueStore;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWechatPayService;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Log4j2
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
