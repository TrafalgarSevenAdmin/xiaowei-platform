package com.xiaowei.accountweb.dto;

import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.Department;
import com.xiaowei.account.entity.Post;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(value = "系统用户")
public class SysUserDTO {
    public interface UpdateStatus{}
    public interface UpdatePassword{}

    @ApiModelProperty(value = "登录用户名")
    @Size(min = 2,max = 20,groups = {V.Insert.class,V.Update.class},message = "登录名[2-20]位!")
    @NotBlank(groups = {V.Insert.class},message = "登录名必填!")
    @ParamField("登录用户名")
    private String loginName;

    @Size(min = 5,max = 20,groups = {V.Insert.class,UpdatePassword.class},message = "密码[5-20]位!")
    @NotBlank(groups = {V.Insert.class,UpdatePassword.class},message = "密码必填!")
    @ApiModelProperty(value = "用户密码")
    @ParamField("用户密码")
    private String password;

    @Size(min = 5,max = 20,groups = {UpdatePassword.class},message = "原密码[5-20]位!")
    @NotBlank(groups = {UpdatePassword.class},message = "原密码必填!")
    @ApiModelProperty(value = "原密码")
    @ParamField("原密码")
    private String oldPassword;

    @Pattern(regexp = "\\d{11}",groups = {V.Insert.class,V.Update.class},message = "手机号11位!")
    @ApiModelProperty(value = "手机号码")
    @ParamField("手机号码")
    private String mobile;

    @ApiModelProperty(value = "电子邮箱")
    @ParamField("电子邮箱")
    private String email;

    @ApiModelProperty(value = "用户状态:0代表正常,1代表禁用")
    @NotNull(groups = {SysUserDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {SysUserDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;

    @ApiModelProperty(value = "用户的角色")
    private List<SysRole> roles;

    @ApiModelProperty(value = "用户的公司")
    private Company company;

    @ApiModelProperty(value = "用户的部门")
    @ParamField("用户的部门")
    private Department department;

    @ApiModelProperty(value = "用户的岗位")
    @ParamField("用户的岗位")
    private Post post;

    @ApiModelProperty(value = "真实名称")
    @ParamField("真实名称")
    private String nickName;
    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证")
    @ParamField("身份证")
    private String card;

}
