package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.WorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ServiceItemDTO implements Serializable{
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
     * 收费
     */
    @ApiModelProperty(value = "收费")
    private Double toll;

    /**
     * 是否需要审核
     */
    @ApiModelProperty(value = "是否需要审核")
    private Boolean audit;

}
