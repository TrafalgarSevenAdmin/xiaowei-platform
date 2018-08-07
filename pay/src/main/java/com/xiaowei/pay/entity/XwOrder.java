package com.xiaowei.pay.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.mq.constant.MqQueueConstant;
import com.xiaowei.pay.consts.OrderType;
import com.xiaowei.pay.consts.PayStatus;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "p_order")
public class XwOrder extends BaseEntity {

    /**
     * 业务id
     */
    private String businessId;


    /**
     * 所属用户
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private SysUser user;


    /**
     * 订单类型，默认值
     */
    private String type = OrderType.workOrder;


    /**
     * <pre>
     * 字段名：商品描述.
     * 变量名：Name
     * 是否必填：是
     * 类型：String(128)
     * 示例值： 腾讯充值中心-QQ会员充值
     * 描述：商品简单描述，该字段须严格按照规范传递，具体请见参数规定
     * </pre>
     */
    private String name;

    /**
     * <pre>
     * 字段名：总金额.
     * 变量名：total_fee
     * 是否必填：是
     * 类型：Int
     * 示例值： 888
     * 描述：订单总金额，单位为分，详见支付金额
     * </pre>
     */
    private Integer totalFee;

    /**
     * <pre>
     * 字段名：交易起始时间(订单生成时间)
     * </pre>
     */
    private Date timeStart = new Date();

    /**
     * <pre>
     * 字段名：交易过期时间，（不小于5分钟）,默认两个小时后
     * 测试时使用5分钟
     * </pre>
     */
    private Date timeExpire = DateUtils.addMinutes(new Date(),5);

    /**
     * 订单状态
     * 默认待付款
     */
    private PayStatus status = PayStatus.created;

    /**
     * 存放的消息
     */
    private String message;

    /**
     * 成功支付通知的路由
     */
    private String queue =  MqQueueConstant.ORDER_DEFAULT_PAYED_QUEUE;

    /**
     * 支付结果
     */
    @ManyToOne(targetEntity = XwOrderResult.class,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "result_id")
    @Fetch(FetchMode.JOIN)
    private XwOrderResult result;

    /**
     * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
     * 方便重复支付
     */
    private String prepayId;

    public XwOrder() {
    }

    public XwOrder(String businessId, SysUser user, String name, Integer totalFee, Date timeExpire) {
        this.businessId = businessId;
        this.user = user;
        this.name = name;
        this.totalFee = totalFee;
        this.timeExpire = timeExpire;
    }
    public XwOrder(String businessId,SysUser user, String name, Integer totalFee) {
        this.businessId = businessId;
        this.user = user;
        this.name = name;
        this.totalFee = totalFee;
    }
}
