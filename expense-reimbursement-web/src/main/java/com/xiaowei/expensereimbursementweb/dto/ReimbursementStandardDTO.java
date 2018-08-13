package com.xiaowei.expensereimbursementweb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReimbursementStandardDTO {
    /**
     * 费用科目编号
     */
    @ApiModelProperty(value = "费用科目编号")
    private String subjectCode;
    /**
     * 舱位级别
     */
    @ApiModelProperty(value = "舱位级别")
    private String shipLevel;
    /**
     * 出行方式
     */
    @ApiModelProperty(value = "出行方式")
    private String outType;
    /**
     * 城市级别
     */
    @ApiModelProperty(value = "城市级别")
    private String cityLevel;
    /**
     * 岗位级别
     */
    @ApiModelProperty(value = "岗位级别")
    private String postLevel;
    /**
     * 出发城市
     */
    @ApiModelProperty(value = "出发城市")
    private String startCity;
    /**
     * 到达城市
     */
    @ApiModelProperty(value = "到达城市")
    private String endCity;
    /**
     * 计费类型
     */
    @ApiModelProperty(value = "计费类型")
    private Integer costType;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private Double unitCost;
}
