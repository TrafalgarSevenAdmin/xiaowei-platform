package com.xiaowei.accountweb.dto;

import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.core.constants.PatternConstant;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "系统用户")
public class SysUserDTO {
    public interface UpdateStatus{}

    @ApiModelProperty(value = "登录用户名")
    @Size(min = 2,max = 20,groups = {V.Insert.class,V.Update.class},message = "登录名[2-20]位!")
    @NotBlank(groups = {V.Insert.class},message = "登录名必填!")
    private String loginName;

    @Size(min = 5,max = 20,groups = {V.Insert.class},message = "密码[5-20]位!")
    @NotBlank(groups = {V.Insert.class},message = "密码必填!")
    @ApiModelProperty(value = "用户密码")
    private String password;

    @Pattern(regexp = "\\d{11}",groups = {V.Insert.class,V.Update.class},message = "手机号11位!")
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @Pattern(regexp = PatternConstant.EMAIL,groups = {V.Insert.class,V.Update.class},message = "邮箱格式不正确")
    @ApiModelProperty(value = "修改邮箱")
    private String email;

    @ApiModelProperty(value = "用户状态:0代表正常,1代表禁用")
    @NotNull(groups = {SysUserDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {SysUserDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;

    @ApiModelProperty(value = "用户的角色")
    private List<SysRole> roles;

    @ApiModelProperty(value = "用户的公司")
    private List<Company> companies;

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

}
