package com.xiaowei.account.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "系统用户信息")
public class SysUserInfoBean {

    @ApiModelProperty(value = "用户Id",hidden = true)
    private String uid;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "修改邮箱")
    private String email;

    @ApiModelProperty(value = "是否激活")
    private boolean enabled;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
