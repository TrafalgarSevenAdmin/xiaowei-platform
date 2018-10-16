package com.xiaowei.worksystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.WorkOrderType;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestWorkOrderDTO {
    public interface UpdateStatus{}

    /**
     * 流程模板
     */
    @ApiModelProperty(value = "流程模板")
    @ParamField("流程模板")
    private WorkFlow workFlow;

    /**
     * 所属设备
     */
    @ApiModelProperty(value = "所属设备")
    @ParamField("所属设备")
    private Equipment equipment;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    @ParamField("联系人")
    private String linkMan;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @ParamField("联系电话")
    private String linkPhone;
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
     * 要求
     */
    @ApiModelProperty(value = "要求")
    @ParamField("要求")
    private String demand;
    /**
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "服务类型必填!")
    @ParamField("服务类型")
    private WorkOrderType workOrderType;
    /**
     * 申请处理人
     */
    @ApiModelProperty(value = "申请处理人")
    @ParamField("申请处理人")
    private SysUser proposer;

    /**
     * 处理工程师
     */
    @ApiModelProperty(value = "处理工程师")
    @ParamField("处理工程师")
    private SysUser engineer;

    /**
     * 报修文件id(多文件以分号隔开)
     */
    @ApiModelProperty(value = "报修文件id(多文件以分号隔开)")
    private String repairFileStore;
    /**
     * 报修音频id(多文件以分号隔开)
     */
    @ApiModelProperty(value = "报修音频id(多文件以分号隔开)")
    private String repairVoice;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @NotNull(groups = {UpdateStatus.class},message = "状态必填!")
    private Integer status;

    /**
     * 地图定位
     */
    @ApiModelProperty(value = "地图定位")
    private String wkt;

    /**
     * 服务对象
     */
    @ApiModelProperty(value = "服务对象")
    private Customer customer;

    /**
     * 区县
     */
    @ApiModelProperty(value = "区县")
    private String county;
}
