package com.xiaowei.account.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统角色表
 */
@Data
@Table(name = "sys_role")
@Entity
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 说明
     */
    private String comment;

    /**
     * 角色和权限多对多
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_role_permission",
            joinColumns={@JoinColumn(name="ROLE_ID")},
            inverseJoinColumns={@JoinColumn(name="PERMISSION_ID")})
    @JsonIgnore
    private List<SysPermission> permissions;

    /**
     * 角色所包含的用户
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "roles")
    @JsonIgnore
    private List<SysUser> users;

    /**
     * 角色完整代码
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 所属公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;

    @Override
    public String toString() {
        return "SysRole{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", permissions=" + permissions.stream().map(SysPermission::getSymbol).collect(Collectors.toList()) +
                ", users=" + users.stream().map(SysUser::getNickName).collect(Collectors.toList()) +
                ", code='" + code + '\'' +
                ", roleType=" + roleType +
                ", company=" + company +
                '}';
    }
}
