package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DepartWorkOrderDTO {
    @ApiModelProperty(value = "到达图片")
    @NotBlank(groups = {V.Insert.class},message = "到达图片必填!")
    private String arriveFileStore;

    @ApiModelProperty(value = "到达地点wkt")
    @NotBlank(groups = {V.Insert.class},message = "到达地点wkt必填!")
    private String wkt;


}
