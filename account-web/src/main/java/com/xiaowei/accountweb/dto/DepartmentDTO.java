package com.xiaowei.accountweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DepartmentDTO {
    public interface UpdateStatus{}

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "部门名称必填!")
    private String departmentName;

    /**
     * 部门logo
     */
    @ApiModelProperty(value = "部门logo")
    private String logo;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "部门状态:0代表正常,1代表禁用")
    @NotNull(groups = {CompanyDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {DepartmentDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;

    /**
     * 所属公司
     */
    @ApiModelProperty(value = "所属公司")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "所属公司为空!")
    private Company company;

}
