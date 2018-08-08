package com.xiaowei.pay.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "p_order_result")
public class XwOrderResult  extends BaseEntity {

    /**
     * <pre>
     * 字段名：营销详情.
     * 变量名：promotion_detail
     * 是否必填：否，单品优惠才有
     * 类型：String(6000)
     * 示例值：[{"promotion_detail":[{"promotion_id":"109519","name":"单品惠-6","scope":"SINGLE","type":"DISCOUNT","amount":5,"activity_id":"931386","wxpay_contribute":0,"merchant_contribute":0,"other_contribute":5,"goods_detail":[{"goods_id":"a_goods1","goods_remark":"商品备注","quantity":7,"price":1,"discount_amount":4},{"goods_id":"a_goods2","goods_remark":"商品备注","quantity":1,"price":2,"discount_amount":1}]}]}
     * 描述：单品优惠专用参数，详见https://pay.weixin.qq.com/wiki/doc/api/danpin.php?chapter=9_203&index=4
     * </pre>
     */
    private String promotionDetail;

    /**
     * <pre>
     * 字段名：设备号.
     * 变量名：device_info
     * 是否必填：否
     * 类型：String(32)
     * 示例值：013467007045764
     * 描述：微信支付分配的终端设备号，
     * </pre>
     */
    private String deviceInfo;

    /**
     * <pre>
     * 字段名：用户标识.
     * 变量名：openid
     * 是否必填：是
     * 类型：String(128)
     * 示例值：wxd930ea5d5a258f4f
     * 描述：用户在商户appid下的唯一标识
     * </pre>
     */
    private String openid;

    /**
     * <pre>
     * 字段名：是否关注公众账号.
     * 变量名：is_subscribe
     * 是否必填：否
     * 类型：String(1)
     * 示例值：Y
     * 描述：用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     * </pre>
     */
    private String isSubscribe;

    /**
     * <pre>
     * 字段名：用户子标识.
     * 变量名：sub_openid
     * 是否必填：是
     * 类型：String(128)
     * 示例值：wxd930ea5d5a258f4f
     * 描述：用户在子商户appid下的唯一标识
     * </pre>
     */
    private String subOpenid;

    /**
     * <pre>
     * 字段名：是否关注子公众账号.
     * 变量名：sub_is_subscribe
     * 是否必填：否
     * 类型：String(1)
     * 示例值：Y
     * 描述：用户是否关注子公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
     * </pre>
     */
    private String subIsSubscribe;


    /**
     * <pre>
     * 字段名：交易类型.
     * 变量名：trade_type
     * 是否必填：是
     * 类型：String(16)
     * 示例值：JSAPI
     * JSA描述：PI、NATIVE、APP
     * </pre>
     */
    private String tradeType;

    /**
     * <pre>
     * 字段名：付款银行.
     * 变量名：bank_type
     * 是否必填：是
     * 类型：String(16)
     * 示例值：CMC
     * 描述：银行类型，采用字符串类型的银行标识，银行类型见银行列表
     * </pre>
     */
    private String bankType;

    /**
     * <pre>
     * 字段名：订单金额.
     * 变量名：total_fee
     * 是否必填：是
     * 类型：Int
     * 示例值：100
     * 描述：订单总金额，单位为分
     * </pre>
     */
    private Integer totalFee;

    /**
     * <pre>
     * 字段名：应结订单金额.
     * 变量名：settlement_total_fee
     * 是否必填：否
     * 类型：Int
     * 示例值：100
     * 描述：应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     * </pre>
     */
    private Integer settlementTotalFee;

    /**
     * <pre>
     * 字段名：货币种类.
     * 变量名：fee_type
     * 是否必填：否
     * 类型：String(8)
     * 示例值：CNY
     * 描述：货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * </pre>
     */
    private String feeType;

    /**
     * <pre>
     * 字段名：现金支付金额.
     * 变量名：cash_fee
     * 是否必填：是
     * 类型：Int
     * 示例值：100
     * 描述：现金支付金额订单现金支付金额，详见支付金额
     * </pre>
     */
    private Integer cashFee;

    /**
     * <pre>
     * 字段名：现金支付货币类型.
     * 变量名：cash_fee_type
     * 是否必填：否
     * 类型：String(16)
     * 示例值：CNY
     * 描述：货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * </pre>
     */
    private String cashFeeType;
    /**
     * <pre>
     * 字段名：总代金券金额.
     * 变量名：coupon_fee
     * 是否必填：否
     * 类型：Int
     * 示例值：10
     * 描述：代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
     * </pre>
     */
    private Integer couponFee;

    /**
     * <pre>
     * 字段名：代金券使用数量.
     * 变量名：coupon_count
     * 是否必填：否
     * 类型：Int
     * 示例值：1
     * 描述：代金券使用数量
     * </pre>
     */
    private Integer couponCount;

    /**
     * <pre>
     * 字段名：微信支付订单号.
     * 变量名：transaction_id
     * 是否必填：是
     * 类型：String(32)
     * 示例值：1217752501201407033233368018
     * 描述：微信支付订单号
     * </pre>
     */
    private String transactionId;

    /**
     * <pre>
     * 字段名：商户订单号.
     * 变量名：out_trade_no
     * 是否必填：是
     * 类型：String(32)
     * 示例值：1212321211201407033568112322
     * 描述：商户系统的订单号，与请求一致。
     * </pre>
     */
    private String outTradeNo;

    /**
     * <pre>
     * 字段名：商家数据包.
     * 变量名：attach
     * 是否必填：否
     * 类型：String(128)
     * 示例值：123456
     * 描述：商家数据包，原样返回
     * </pre>
     */
    private String attach;

    /**
     * <pre>
     * 字段名：支付完成时间.
     * 示例值：20141030133525
     * 描述：支付完成时间
     * </pre>
     */
    private Date payTime;

    /**
     * <pre>
     * 字段名：接口版本号.
     * 变量名：version
     * 类型：String(32)
     * 示例值：1.0
     * 更多信息，详见文档：https://pay.weixin.qq.com/wiki/doc/api/danpin.php?chapter=9_101&index=1
     * </pre>
     */
    private String version;

    /**
     * 回调来源ip
     */
    private String ip;
}
