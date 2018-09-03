package com.xiaowei.accountweb.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CompanyDTO {
    public interface UpdateStatus{}

    @ApiModelProperty(value = "公司名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "公司名称必填!")
    @ParamField("公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司地址")
    @NotBlank(message = "公司地址必填!")
    @ParamField("公司地址")
    private String address;

    @ApiModelProperty(value = "公司logo")
    private String logo;

    @ApiModelProperty(value = "负责人")
    @ParamField("负责人")
    private SysUser principal;

    @ApiModelProperty(value = "简介")
    @ParamField("简介")
    private String intro;

    @ApiModelProperty(value = "公司状态:0代表正常,1代表禁用")
    @NotNull(groups = {CompanyDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {CompanyDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;

}
