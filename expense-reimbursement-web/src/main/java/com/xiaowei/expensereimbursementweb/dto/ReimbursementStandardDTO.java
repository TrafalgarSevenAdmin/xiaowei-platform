package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReimbursementStandardDTO {
    /**
     * 费用科目编号
     */
    @ApiModelProperty(value = "费用科目编号")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "费用科目编号必填!")
    private String subjectCode;
    /**
     * 舱位级别
     */
    @ApiModelProperty(value = "舱位级别")
    private String shipLevel;
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
     * 单价
     */
    @ApiModelProperty(value = "单价")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "单价必填!")
    private Double unitCost;
}
