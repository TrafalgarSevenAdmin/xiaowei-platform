package com.xiaowei.worksystem.entity.customer;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 服务对象联系人
 */
@Data
@Entity
@Table(name = "W_CUSTOMER_USER")
@SQLDelete(sql = "update w_customer_user set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class CustomerUser extends MultiBaseEntity {

    /**
     * 用户名
     */
    String userName;

    /**
     * 部门
     */
    String department;

    /**
     * 性别
     */
    String gender;

    /**
     * 职位
     */
    String position;

    /**
     * 用户类型（行长、网点负责人、设备管理员、客户经理、其它）
     */
    String userType;

    /**
     * 办公电话
     */
    String officeTel;

    /**
     * 手机
     */
    String mobile;

    /**
     * 邮箱
     */
    String email;

    /**
     * 联系地址
     */
    String address;

    /**
     * 微信
     */
    String wx;

    /**
     * QQ
     */
    String qq;

    /**
     * 绑定的用户（如果有）
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    SysUser user;

    /**
     * 所属服务对象
     */
    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    @Fetch(FetchMode.JOIN)
    Customer customer;

}