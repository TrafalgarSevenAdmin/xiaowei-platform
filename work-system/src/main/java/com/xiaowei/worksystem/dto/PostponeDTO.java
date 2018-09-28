package com.xiaowei.worksystem.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PostponeDTO {
    /**
     * 预约时间
     */
    @ParamField("预约时间")
    @ApiModelProperty(value = "预约时间")
    private Date appointTime;
    /**
     * 预计完成时间
     */
    @ParamField("预计完成时间")
    @ApiModelProperty(value = "预计完成时间")
    private Date preFinishedTime;
}
