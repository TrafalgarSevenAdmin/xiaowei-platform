package com.xiaowei.worksystem.entity.customer;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实体
 */
@Data
@Entity
@Table(name = "W_CUSTOMER")
@SQLDelete(sql = "update w_customer set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Customer extends BaseEntity {

    /**
     * 客户名称
     */
    String customerName;

    /**
     * 省份
     */
    String province;

    /**
     * 区县
     */
    String county;

    /**
     * 城市
     */
    String city;

    /**
     * 客户地址
     */
    String address;

    /**
     * 邮政编码
     */
    String postCode;

    /**
     * 办公电话
     */
    String officeTel;

    /**
     * 银行帐号
     */
    String bankAccount;

    /**
     * 开户行
     */
    String bank;

    /**
     * 备注
     */
    String remark;

    /**
     * 客户分类（国有银行、股份制银行、商业银行、信用社..）
     */
    String customerType;

    /**
     * 创建人
     */
    String createUser;

    /**
     * 客户编码
     */
    String customerCode;

    /**
     * 税号
     */
    String taxCode;
}