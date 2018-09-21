package com.xiaowei.worksystem.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FinishInhandWorkOrderDTO {

    /**
     * 完成情况
     */
    @ApiModelProperty(value = "完成情况")
    @ParamField("质检文件id")
    private String state;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    @ParamField("图片")
    private String arriveFileStore;

}
