package com.xiaowei.accountcommon;


import java.io.Serializable;

/**
 * 角色的值对象
 */
public class RoleBean implements Serializable {

    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 说明
     */
    private String comment;

    /**
     * 角色完整代码
     */
    private String code;
    /**
     * 父级代码
     */
    private String parentCode;
    /**
     * 角色代码
     */
    private Integer ownCode;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
