package com.xiaowei.accountweb.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "系统权限")
public class SysPermissionDTO {

    @ApiModelProperty(value = "权限名称")
    @Size(min = 1,max = 20,groups = {V.Insert.class,V.Update.class},message = "权限名[1-20]位!")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "权限名称必填!")
    private String name;

    @ApiModelProperty(value = "权限父级代码")
    @NotBlank(groups = {V.Insert.class},message = "父级代码不能为空!")
    private String parentCode;

    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "权限标识必填!")
    @ApiModelProperty(value = "权限标识")
    private String symbol;

    @ApiModelProperty(value = "权限前置条件")
    private String precondition;

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
