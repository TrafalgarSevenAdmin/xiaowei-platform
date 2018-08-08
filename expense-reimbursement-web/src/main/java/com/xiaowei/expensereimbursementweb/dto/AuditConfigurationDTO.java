package com.xiaowei.expensereimbursementweb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditConfigurationDTO {
    /**
     * 审核配置用户
     */
    @ApiModelProperty(value = "审核配置用户")
    private String userId;
    /**
     * 审核配置部门
     */
    @ApiModelProperty(value = "审核配置部门")
    private String departmentId;
    /**
     * 审核配置类型状态
     */
    @ApiModelProperty(value = "审核配置类型状态")
    private Integer typeStatus;
}
