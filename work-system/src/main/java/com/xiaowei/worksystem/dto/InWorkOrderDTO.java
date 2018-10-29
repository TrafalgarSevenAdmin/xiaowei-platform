package com.xiaowei.worksystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.WorkOrderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class InWorkOrderDTO {

    /**
     * 故障描述
     */
    @ApiModelProperty(value = "故障描述")
    @ParamField("故障描述")
    private String errorDescription;

    /**
     * 工单标题
     */
    @ApiModelProperty(value = "工单标题")
    @ParamField("工单标题")
    private String workOrderTitle;

    /**
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "服务类型必填!")
    @ParamField("服务类型")
    private WorkOrderType workOrderType;

    /**
     * 后台处理人
     */
    @ApiModelProperty(value = "后台处理人")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "后台处理人必填!")
    @ParamField("后台处理人")
    private SysUser backgrounder;

    /**
     * 工单要求
     */
    @ApiModelProperty(value = "工单要求")
    @ParamField("工单要求")
    private String demand;

    /**
     * 处理工程师
     */
    @ApiModelProperty(value = "处理工程师")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "处理工程师必填!")
    @ParamField("处理工程师")
    private SysUser engineer;

    /**
     * 预计完成时间
     */
    @ApiModelProperty(value = "预计完成时间")
    private Date preFinishedTime;

}
