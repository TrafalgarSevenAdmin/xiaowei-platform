package com.xiaowei.accountweb.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AllAuditConfigurationDTO {
    /**
     * 审核配置用户
     */
    @ApiModelProperty(value = "审核配置用户")
    @NotEmpty(groups = {V.Insert.class,V.Update.class},message = "审核配置用户必填!")
    @ParamField("审核配置用户")
    private List<String> userIds;
    /**
     * 审核配置部门
     */
    @ApiModelProperty(value = "审核配置部门")
    @NotEmpty(groups = {V.Insert.class,V.Update.class},message = "审核配置部门必填!")
    @ParamField("审核配置部门")
    private List<String> departmentIds;
    /**
     * 审核配置类型状态
     */
    @ApiModelProperty(value = "审核配置类型状态")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "审核配置类型状态必填!")
    @ParamField("审核配置类型状态")
    private Integer typeStatus;
}
