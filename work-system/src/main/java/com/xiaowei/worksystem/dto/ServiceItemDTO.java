package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.WorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;

@Data
public class ServiceItemDTO implements Serializable{
    private String code;
    /**
     * 是否收费
     */
    @Valid
    @ApiModelProperty(value = "是否收费")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "是否收费必填!")
    private Boolean charge;


    /**
     * 维修类型
     */
    @Valid
    @ApiModelProperty(value = "维修类型")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "维修类型必填!")
    private String serviceType;
    /**
     * 维修简介
     */
    @ApiModelProperty(value = "维修简介")
    private String serviceIntro;


    /**
     * 所属工单
     */
    @ApiModelProperty(value = "所属工单")
    private WorkOrder workOrder;

    /**
     * 保内收费
     */
    @ApiModelProperty(value = "保内收费")
    private Double toll;
    /**
     * 保外收费
     */
    @ApiModelProperty(value = "保外收费")
    private Double outToll;

    /**
     * 是否需要审核
     */
    @ApiModelProperty(value = "是否需要审核")
    private Boolean audit;

    /**
     * 预计时长
     */
    @ApiModelProperty(value = "预计时长")
    private Time predictTime;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    /**
     * 工作标准说明
     */
    @ApiModelProperty(value = "工作标准说明")
    private String standard;

}
