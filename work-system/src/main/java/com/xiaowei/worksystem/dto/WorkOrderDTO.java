package com.xiaowei.worksystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.WorkOrderType;
import com.xiaowei.worksystem.entity.customer.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class WorkOrderDTO {
    public interface DistributeWorkOrder{}

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
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "联系人必填!")
    @ParamField("联系人")
    private String linkMan;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "联系电话必填!")
    @Pattern(regexp = "\\d{11}",groups = {V.Insert.class,V.Update.class},message = "手机号11位!")
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
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "服务类型必填!")
    @ParamField("服务类型")
    private WorkOrderType workOrderType;

    /**
     * 针对后台处理人员状态
     */
    @ApiModelProperty(value = "针对后台处理人员状态")
    private Integer systemStatus;
    /**
     * 针对用户状态
     */
    @ApiModelProperty(value = "针对用户状态")
    private Integer userStatus;
    /**
     * 申请处理人
     */
    @ApiModelProperty(value = "申请处理人")
    @ParamField("proposer")
    private SysUser proposer;
    /**
     * 后台处理人
     */
    @ApiModelProperty(value = "后台处理人")
    @NotNull(groups = {V.Insert.class,V.Update.class,WorkOrderDTO.DistributeWorkOrder.class},message = "后台处理人必填!")
    @ParamField("后台处理人")
    private SysUser backgrounder;
    /**
     * 处理工程师
     */
    @ApiModelProperty(value = "处理工程师")
    @NotNull(groups = {V.Insert.class,V.Update.class,WorkOrderDTO.DistributeWorkOrder.class},message = "处理工程师必填!")
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
     * 地图定位
     */
    @ApiModelProperty(value = "地图定位")
    private String wkt;

    /**
     * 服务对象
     */
    @ApiModelProperty(value = "服务对象")
    @ParamField("服务对象")
    private Customer customer;

    /**
     * 区县
     */
    @ApiModelProperty(value = "区县")
    @ParamField("区县")
    private String county;

    /**
     * 预计完成时间
     */
    private Date preFinishedTime;

}
