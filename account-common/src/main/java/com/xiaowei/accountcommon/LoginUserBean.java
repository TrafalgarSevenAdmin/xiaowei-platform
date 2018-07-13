package com.xiaowei.accountcommon;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LoginUserBean implements Serializable {

    private static final long serialVersionUID = -4112542381818773302L;

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

    private List<DepartmentBean> departmentBeans;

    public LoginUserBean() {
    }

    public LoginUserBean(String id,String loginName, String mobile, String email, Integer status, String nickName,List<RoleBean> roles, List<PermissionBean> permissions,List<CompanyBean> companyBeans,
                         List<DepartmentBean> departmentBeans) {
        this.id = id;
        this.loginName = loginName;
        this.mobile = mobile;
        this.email = email;
        this.status = status;
        this.nickName = nickName;
        this.roles = roles;
        this.permissions = permissions;
        this.companyBeans = companyBeans;
        this.departmentBeans = departmentBeans;
    }
}
