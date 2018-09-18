package com.xiaowei.worksystem.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.status.ServiceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WorkOrderTypeDTO {

    @ApiModelProperty(value = "名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "名称必填!")
    @ParamField("名称")
    private String name;

    @ApiModelProperty(value = "类型")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "类型必填!")
    @ParamField("类型")
    private ServiceType serviceType;
}
