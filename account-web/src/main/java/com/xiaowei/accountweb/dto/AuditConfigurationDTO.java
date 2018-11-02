package com.xiaowei.accountweb.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AuditConfigurationDTO {
    /**
     * 审核配置用户
     */
    @ApiModelProperty(value = "审核配置用户")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "审核配置用户必填!")
    @ParamField("审核配置用户")
    private String userId;
    /**
     * 审核配置部门
     */
    @ApiModelProperty(value = "审核配置部门")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "审核配置部门必填!")
    @ParamField("审核配置部门")
    private String departmentId;
    /**
     * 审核配置类型状态
     */
    @ApiModelProperty(value = "审核配置类型状态")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "审核配置类型状态必填!")
    @ParamField("审核配置类型状态")
    private Integer typeStatus;
}
