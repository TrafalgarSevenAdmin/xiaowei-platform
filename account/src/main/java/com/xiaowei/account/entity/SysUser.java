package com.xiaowei.account.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * @author 系统用户表
 * @Date 2018-03-20 14:49:59
 * @Description 认证成功之后处理
 * @Version 1.0
 */
@Data
@Table(name = "sys_user")
@Entity
public class SysUser extends BaseEntity {

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 真实名称
     */
    private String nickName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态  0代表正常  1代表禁用  2代表删除
     */
    private Integer status;

    /**
     * 用户和角色多对多
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_user_role",
        joinColumns={@JoinColumn(name="USER_ID")},
        inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    @JsonIgnore
    private List<SysRole> roles;

    /**
     * 用户与公司的多对多
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_user_company",
            joinColumns={@JoinColumn(name="USER_ID")},
            inverseJoinColumns={@JoinColumn(name="COMPANY_ID")})
    @JsonIgnore
    private List<Company> companies;

}
