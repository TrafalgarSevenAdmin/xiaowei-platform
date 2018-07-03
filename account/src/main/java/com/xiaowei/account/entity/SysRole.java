package com.xiaowei.account.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * @author mocker
 * @Date 2018-03-21 15:11:05
 * @Description 系统角色表
 * @Version 1.0
 */
@Table(name = "sys_role")
@Entity
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(unique = true)
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
     * 角色的子级
     */
    @Transient
    private List<SysRole> children;

    /**
     * 所属公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;


    public List<SysRole> getChildren() {
        return children;
    }

    public void setChildren(List<SysRole> children) {
        this.children = children;
    }

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<SysPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SysPermission> permissions) {
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
