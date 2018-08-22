package com.xiaowei.expensereimbursementweb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShipLevelDTO {
    /**
     * 所属科目编号
     */
    @ApiModelProperty(value = "所属科目编号")
    private String subjectCode;
    /**
     * 舱位级别
     */
    @ApiModelProperty(value = "舱位级别")
    private String shipLevel;
}
