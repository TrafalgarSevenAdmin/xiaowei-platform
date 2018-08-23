package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "账户信息")
public class LoginSysUserDTO {

    @ApiModelProperty(value = "登录用户名")
    @Size(min = 2,max = 20,message = "登录名[2-20]位!")
    @NotBlank(message = "登录名必填!")
    private String loginName;

    @Size(min = 5,max = 20,message = "密码[5-20]位!")
    @NotBlank(message = "密码必填!")
    @ApiModelProperty(value = "用户密码")
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
