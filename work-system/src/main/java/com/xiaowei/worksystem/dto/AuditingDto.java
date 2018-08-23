package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuditingDto {

    @ApiModelProperty(value = "任务id")
    @NotBlank(message = "任务id必填!")
    String taskId;

    @ApiModelProperty(value = "下一个节点id",notes = "用于自由驳回时使用")
    String nextNodeId;

    @ApiModelProperty(value = "通过/不通过")
    Boolean pass = true;

    @ApiModelProperty(value = "理由")
    String reason;

    @ApiModelProperty(value = "附加数据")
    String ext;
}
