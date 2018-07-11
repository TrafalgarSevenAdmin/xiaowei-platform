package com.xiaowei.accountcommon;

import java.io.Serializable;
import java.util.List;

public class LoginUserBean implements Serializable {

    private String id;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实名称
     */
    private String nickName;

    private Integer status;

    private List<RoleBean> roles;

    private List<PermissionBean> permissions;

    private List<CompanyBean> companyBeans;

    public LoginUserBean() {
    }

    public LoginUserBean(String id,String loginName, String mobile, String email, Integer status, String nickName,List<RoleBean> roles, List<PermissionBean> permissions,List<CompanyBean> companyBeans) {
        this.id = id;
        this.loginName = loginName;
        this.mobile = mobile;
        this.email = email;
        this.status = status;
        this.nickName = nickName;
        this.roles = roles;
        this.permissions = permissions;
        this.companyBeans = companyBeans;
    }


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<RoleBean> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleBean> roles) {
        this.roles = roles;
    }

    public List<PermissionBean> getPermissions() {
        return permissions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CompanyBean> getCompanyBeans() {
        return companyBeans;
    }

    public void setCompanyBeans(List<CompanyBean> companyBeans) {
        this.companyBeans = companyBeans;
    }

    public void setPermissions(List<PermissionBean> permissions) {
        this.permissions = permissions;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
