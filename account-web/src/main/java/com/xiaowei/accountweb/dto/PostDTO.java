package com.xiaowei.accountweb.dto;

import com.xiaowei.account.entity.Company;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostDTO {
    public interface UpdateStatus{}

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "岗位名称必填!")
    @ParamField("岗位名称")
    private String postName;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    @ParamField("简介")
    private String intro;
    /**
     * 岗位级别
     */
    @ApiModelProperty(value = "岗位级别")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "岗位级别必填!")
    @ParamField("岗位级别")
    private String level;
    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "所属公司必填!")
    @ParamField("所属公司")
    private Company company;

    @ApiModelProperty(value = "岗位状态:0代表正常,1代表禁用")
    @NotNull(groups = {CompanyDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {DepartmentDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;
}
