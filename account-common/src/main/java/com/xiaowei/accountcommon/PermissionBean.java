package com.xiaowei.accountcommon;

import java.io.Serializable;

public class PermissionBean implements Serializable {

    private String id;

    /**
     * 权限名称
     */
    private String name;

    private String symbol;

    /**
     * 权限完整代码
     */
    private String code;

    /**
     * 父级代码
     */
    private String parentCode;

    /**
     * 权限代码
     */
    private Integer ownCode;

    /**
     * 分配权限的前置条件 比如要修改用户的权限 则默认分配用户列表的权限
     */
    private String precondition;

    /**
     * 权限的等级
     */
    private Integer level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
