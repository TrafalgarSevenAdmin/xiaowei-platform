package com.xiaowei.worksystem.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExecuteServiceItemDTO {

    @ApiModelProperty(value = "质检文件id(多文件以分号隔开)")
    @ParamField("质检文件id")
    String qualityFileStore;

    /**
     * 完成服务项目的描述
     */
    @ApiModelProperty(value = "完成服务项目的描述")
    @ParamField("完成服务项目的描述")
    private String endingState;
}
