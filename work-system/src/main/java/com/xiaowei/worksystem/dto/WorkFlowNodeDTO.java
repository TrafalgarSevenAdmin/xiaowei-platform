package com.xiaowei.worksystem.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.Date;

@Data
public class WorkFlowNodeDTO {
    /**
     * 是否收费
     */
    @ApiModelProperty(value = "是否收费")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "是否收费必填!")
    private Boolean isCharge;
    /**
     * 维修类型
     */
    @ApiModelProperty(value = "维修类型")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "维修类型必填!")
    private String serviceType;
    /**
     * 维修简介
     */
    private String serviceIntro;
    /**
     * 工作标准说明
     */
    private String standard;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 收费
     */
    private Double toll;

    /**
     * 是否需要审核
     */
    @ApiModelProperty(value = "是否需要审核")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "是否需要审核必填!")
    private Boolean audit;

    /**
     * 预计时长
     */
    private Time predictTime;
    /**
     * 版本号
     */
    private String version;
    /**
     * 创建时间
     */
    private Date createdTime;
}
