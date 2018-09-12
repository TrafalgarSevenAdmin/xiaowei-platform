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
 * @Description 系统权限
 * @Version 1.0
 */
@Table(name = "sys_permission")
@Entity
public class SysPermission extends BaseEntity {

    /**
     * 权限名称
     */
    private String name;

    @Column(unique = true)
    private String symbol;
    /**
     * 权限完整代码
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 父级代码
     */
    @Column(updatable = false)
    private String parentCode;
    /**
     * 权限代码
     */
    @Column(updatable = false)
    private Integer ownCode;

    /**
     * 分配权限的前置条件 比如要修改用户的权限 则默认分配用户列表的权限
     */
    private String precondition;

    /**
     * 权限的等级
     */
    @Column(updatable = false)
    private Integer level;

    /**
     * 权限所属的角色
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "permissions")
    @JsonIgnore
    private List<SysRole> roles;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getOwnCode() {
        return ownCode;
    }

    public void setOwnCode(Integer ownCode) {
        this.ownCode = ownCode;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", code='" + code + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", ownCode=" + ownCode +
                ", precondition='" + precondition + '\'' +
                ", level=" + level +
                '}';
    }
}
