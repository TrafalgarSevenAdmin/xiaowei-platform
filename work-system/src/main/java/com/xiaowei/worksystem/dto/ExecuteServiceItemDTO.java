package com.xiaowei.worksystem.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExecuteServiceItemDTO {

    @ApiModelProperty(value = "质检文件id(多文件以分号隔开)")
    String qualityFileStore;

    /**
     * 完成服务项目的描述
     */
    @ApiModelProperty(value = "完成服务项目的描述")
    private String endingState;
}
