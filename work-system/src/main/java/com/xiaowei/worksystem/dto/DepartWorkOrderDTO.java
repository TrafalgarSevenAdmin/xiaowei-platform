package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DepartWorkOrderDTO {
    @ApiModelProperty(value = "到达图片")
    private String arriveFileStore;

    @ApiModelProperty(value = "到达地点wkt")
    private String wkt;

    @ApiModelProperty(value = "到达状态")
    @NotNull(groups = {V.Insert.class},message = "到达状态必填!")
    private Integer arriveStatus;


}
