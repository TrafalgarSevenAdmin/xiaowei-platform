package com.xiaowei.wechat.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.mq.sender.MessagePushSender;
import com.xiaowei.pay.consts.PayStatus;
import com.xiaowei.pay.entity.XwOrder;
import com.xiaowei.pay.entity.XwOrderResult;
import com.xiaowei.pay.service.IOrderService;
import com.xiaowei.wechat.config.WechatProperties;
import com.xiaowei.wechat.consts.MagicValueStore;
import com.xiaowei.wechat.dto.PayType;
import com.xiaowei.wechat.entity.WxUser;
import com.xiaowei.wechat.service.IWechatPayService;
import com.xiaowei.wechat.service.IWxUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class WechatPayServiceImpl implements IWechatPayService {

    /**
     * 交易时间格式化
     */
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private MessagePushSender messagePushSender;

    @Autowired
    private WechatProperties wechatProperties;

    @Override
    public Result createPayOrder(String order, PayType type) {
        XwOrder xwOrder = orderService.findById(order);
        Assert.notNull(xwOrder, "找不到此订单");
        //若此订单尚未提交给微信
        if (xwOrder.getStatus() == PayStatus.created) {
            return commitOrderToWechat(order,type, xwOrder);
        } else if (xwOrder.getStatus() == PayStatus.unpaid) {
            //如果没有生成PrepayId，代码逻辑上不会出现
            if (StringUtils.isBlank(xwOrder.getPrepayId())) {
                log.error("不明原因导致订单数据异常，请检查。异常单号：" + xwOrder.getId());
                throw new BusinessException("订单数据异常！");
            } else {
                //订单已经提交给微信，但未支付
                //重新支付
                return getNeedPayInfo(type, xwOrder.getPrepayId(), xwOrder.getCodeURL());
            }
        } else if (xwOrder.getStatus() == PayStatus.close){
            throw new BusinessException("订单已" + xwOrder.getMessage());
        } else if (xwOrder.getStatus() == PayStatus.paid) {
            throw new BusinessException("订单已支付，请勿重复支付");
        } else {
            throw new BusinessException("订单状态异常");
        }
    }

    private Result commitOrderToWechat(String order,PayType type, XwOrder xwOrder) {
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        EmptyUtils.assertObject(xwOrder, "没有找到订单信息！");
        wxPayUnifiedOrderRequest.setVersion("1.0");
        wxPayUnifiedOrderRequest.setDeviceInfo("WEB");
        wxPayUnifiedOrderRequest.setSpbillCreateIp(ContextUtils.getIpAddr());
        wxPayUnifiedOrderRequest.setBody(xwOrder.getName());
        wxPayUnifiedOrderRequest.setOutTradeNo(xwOrder.getId());
        wxPayUnifiedOrderRequest.setTotalFee(xwOrder.getTotalFee());
        if (xwOrder.getTimeStart() != null) {
            wxPayUnifiedOrderRequest.setTimeStart(timeFormat.format(xwOrder.getTimeStart()));
        }
        if (xwOrder.getTimeExpire() != null) {
            wxPayUnifiedOrderRequest.setTimeExpire(timeFormat.format(xwOrder.getTimeExpire()));
        }
        //使用jsapi公众号中支付
        wxPayUnifiedOrderRequest.setTradeType(type.getType());
        //指定不能使用信用卡支付,因为信用卡支付需要交手续费的
        wxPayUnifiedOrderRequest.setLimitPay("no_credit");
        if (type == PayType.JSAPI) {
            Optional<WxUser> userOptional = wxUserService.findByUserId(xwOrder.getUser().getId());
            EmptyUtils.assertOptional(userOptional, "该用户未绑定微信");
            wxPayUnifiedOrderRequest.setOpenid(userOptional.get().getOpenId());
        } else {
            wxPayUnifiedOrderRequest.setProductId(order);
        }
        try {
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
            if (StringUtils.isBlank(wxPayUnifiedOrderResult.getPrepayId())) {
                throw new BusinessException("获取预支付代码失败！");
            }
            xwOrder.setPrepayId(wxPayUnifiedOrderResult.getPrepayId());
            xwOrder.setCodeURL(wxPayUnifiedOrderResult.getCodeURL());
            //更新本地订单状态为未支付
            xwOrder.setStatus(PayStatus.unpaid);
            //默认两小时
            if (xwOrder.getTimeExpire() == null) {
                xwOrder.setTimeExpire(DateUtils.addHours(new Date(), 2));
            }
            orderService.save(xwOrder);
            //创建延时任务，在配置的时间后若未支付则关闭此订单
            //由于微信会自动关闭超时订单，因此为了防止手动关闭和自动关闭冲突，因此延时10秒
            messagePushSender.sendDelay(MqQueueConstant.DELAY_PAY_TASK_ROUTING, order, (int) (xwOrder.getTimeExpire().getTime() - new Date().getTime()) + 10000);
            String prepayId = wxPayUnifiedOrderResult.getPrepayId();
            String codeURL = wxPayUnifiedOrderResult.getCodeURL();
            return getNeedPayInfo(type, prepayId, codeURL);
        } catch (WxPayException e) {
            log.error("创建订单错误！", e);
            throw new BusinessException("创建订单错误！");
        }
    }

    /**
     * 获取最终的前端需要的支付信息
     * @param type
     * @param prepayId
     * @param codeURL
     * @return
     */
    private Result getNeedPayInfo(PayType type, String prepayId, String codeURL) {
        switch (type) {
            case JSAPI:
                return Result.getSuccess(generateJSAPISign(prepayId));
            case QR:
                return Result.getSuccess(generateQrBase64(codeURL));
        }
        return null;
    }

    /**
     * 处理支付结果信息
     */
    @Override
    public String payInfoHandler(WxPayOrderNotifyResult wxPayOrderNotifyResult,Boolean ipAddrJudge) {
        XwOrderResult result = BeanCopyUtils.copy(wxPayOrderNotifyResult, XwOrderResult.class);
        //个人订单状态
        Optional<XwOrder> xwOrderOptional = orderService.getById(result.getOutTradeNo());
        EmptyUtils.assertOptional(xwOrderOptional, "没有找到订单信息！");
        XwOrder myOrder = xwOrderOptional.get();
        try {
            result.setPayTime(timeFormat.parse(wxPayOrderNotifyResult.getTimeEnd()));
        } catch (ParseException e) {
            log.error("处理时间转换出错",e);
            e.printStackTrace();
        }
        //如果之前的数据显示数据已经支付，那么就更新这个数据
        if (myOrder.getResult() != null) {
            result.setId(myOrder.getResult().getId());
        }
        myOrder.setResult(result);
        //校验回调回来的ip地址,校验支付的最终金额是否一样等等
        if (judgeAttribute(myOrder, result) && (!ipAddrJudge || judgeAttributeIpAddr(myOrder, result))){
            myOrder.setStatus(PayStatus.paid);
            orderService.save(myOrder);
            //回调通知
            if (StringUtils.isNotEmpty(myOrder.getQueue())) {
                messagePushSender.sendOrderPayedMessage(myOrder.getQueue(),result.getOutTradeNo());
            }
            //给微信返回业务处理成功的字段，防止微信一直发送支付结果通知
            return "SUCCESS";
        } else {
            log.warn("校验支付结果出错！订单号："+result.getOutTradeNo());
            orderService.save(myOrder);
            //由于回调错误，随便返回乱码即可。万一是恶意攻击，还可以干扰攻击方
            return "?????";
        }
    }

    /**
     * 关闭订单
     * @param order
     * @param message
     */
    @Override
    public void closeOrder(String order,String message) {
        XwOrder xwOrder = orderService.findById(order);
        EmptyUtils.assertObject(xwOrder, "没有找到订单信息");
        xwOrder.setMessage(message);
        if (xwOrder.getStatus() == PayStatus.created) {
            //未提交给微信时调用,就直接关闭订单
            xwOrder.setStatus(PayStatus.close);
            orderService.save(xwOrder);
        } else if (xwOrder.getStatus() == PayStatus.paid) {
            //若订单显示已经支付
            log.warn("尝试关闭订单："+order+"失败！订单已被支付");
            throw new BusinessException("订单已被支付！关闭失败");
        } else if (xwOrder.getStatus() == PayStatus.unpaid || xwOrder.getStatus() == PayStatus.abnormal) {
            //首先查询一下订单状态，是否被支付，若没有，就关闭
            try {
                WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, order);
                if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS")) {
                    //若订单已经支付，就不能关闭了，同时将此订单状态更新
                    this.payInfoHandler(BeanCopyUtils.copy(wxPayOrderQueryResult, WxPayOrderNotifyResult.class), false);
                    log.warn("微信结果表示订单:" + order + "已被支付！关闭失败");
                    throw new BusinessException("订单已被支付！关闭失败");
                } else {
                    //订单未支付或支付失败,就关闭订单
                    if (!wxPayOrderQueryResult.getTradeState().equals("CLOSED")) {
                        try {
                            wxPayService.closeOrder(order);
                        } catch (Exception e) {
                            log.warn("主动关闭订单失败，可能已经被自动关闭!", e);
                        }
                    }
                    xwOrder.setStatus(PayStatus.close);
                    orderService.save(xwOrder);
                }
            } catch (WxPayException e) {
                log.error("从微信服务中获取订单详情/关单失败！", e);
                throw new BusinessException("获取订单详情失败！");
            }
        }
    }

    /**
     * 校验ip地址
     * @param myOrder
     * @param result
     * @return
     */
    private Boolean judgeAttributeIpAddr(XwOrder myOrder,XwOrderResult result) {
        String ipAddr = ContextUtils.getIpAddr();
        result.setIp(ipAddr);
        return true;
        //这里取消ip来源的校验，因为通过实际测试，发现大多数回调都不是使用的微信提供的ip地址
//        //判断来源的ip地址是否在白名单中
//        Object hostsObject = redisTemplate.opsForValue().get(MagicValueStore.wxHostsList);
//        if (!(hostsObject == null)) {
//            List<String> hosts = (List<String>) hostsObject;
//            if (hosts.contains(ipAddr)) {
//                return true;
//            }
//        }
//        //在获取服务ip地址失败或者redis中不存在此ip名单时，重新获取服务器地址列表，以便及时更新服务器ip
//        try {
//            String[] callbackIP = wxMpService.getCallbackIP();
//            List<String> hosts = Arrays.asList(callbackIP);
//            //更新白名单地址
//            redisTemplate.opsForValue().set(MagicValueStore.wxHostsList, hosts);
//            if (hosts.contains(ipAddr)) {
//                return true;
//            } else {
//                myOrder.setStatus(PayStatus.abnormal);
//                myOrder.setMessage("回调来源不在微信ip列表中！");
//                log.error("回调来源不在微信ip列表中！不排除恶意攻击的可能");
//                return false;
//            }
//        } catch (WxErrorException e) {
//            log.warn("获取微信白名单出错！", e);
//            myOrder.setStatus(PayStatus.abnormal);
//            myOrder.setMessage("无法获取微信ip白名单！");
//            return false;
//        }
    }

    /**
     * 校验其他参数
     * @param myOrder
     * @param result
     * @return
     */
    private Boolean judgeAttribute(XwOrder myOrder, XwOrderResult result) {
        EmptyUtils.assertObject(myOrder, "未找到订单");
        EmptyUtils.assertObject(result, "数据异常");
        if (myOrder.getStatus() != PayStatus.unpaid) {
            throw new BusinessException("订单数据异常，可能已经支付");
        }
        if (NumberUtils.compare(myOrder.getTotalFee(),result.getTotalFee())!=0) {
            myOrder.setStatus(PayStatus.abnormal);
            myOrder.setMessage("支付费用与订单费用不一致！");
            return false;
        }
        return true;
    }

    /**
     * 生成jssdk所需要的配置
     * @param prepayId
     */
    private WxPayMpOrderResult generateJSAPISign(String prepayId) {
        String signType = WxPayConstants.SignType.MD5;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = String.valueOf(System.currentTimeMillis());
        String appid = wechatProperties.getAppId();
        WxPayMpOrderResult payResult = WxPayMpOrderResult.builder()
                .appId(appid)
                .timeStamp(timestamp)
                .nonceStr(nonceStr)
                //在此，由于package被java关键字使用，而替换成了packageValue，因此，前端直接将返回值拿来用是不行的。。
                .packageValue("prepay_id=" + prepayId)
                .signType(signType)
                .build();

        payResult.setPaySign(SignUtils.createSign(payResult, signType, wechatProperties.getMchKey(), false));
        return payResult;
    }

    /**
     * 生成支付二维码的base64
     * @param codeUrl
     */
    private String generateQrBase64(String codeUrl) {
        final byte[] payQrBytes = wxPayService.createScanPayQrcodeMode2(codeUrl, null, null);
        return Base64.getEncoder().encodeToString(payQrBytes);
    }
}
