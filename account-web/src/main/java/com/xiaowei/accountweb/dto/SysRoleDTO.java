package com.xiaowei.accountweb.dto;

import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysPermission;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(value = "系统角色")
public class SysRoleDTO {

    @ApiModelProperty(value = "角色名称")
    @Size(min = 1,max = 20,groups = {V.Insert.class,V.Update.class},message = "角色名[1-20]位!")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "角色名称必填!")
    private String name;

    @ApiModelProperty(value = "角色说明")
    @Size(min = 1,max = 255,groups = {V.Insert.class,V.Update.class},message = "角色说明[1-200]位!")
    private String comment;

    @ApiModelProperty(value = "角色的权限")
    private List<SysPermission> permissions;

    @ApiModelProperty(value = "所属公司")
    private Company company;

}
