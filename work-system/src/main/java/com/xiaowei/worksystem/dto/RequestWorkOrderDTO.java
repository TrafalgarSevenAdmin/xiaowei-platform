package com.xiaowei.worksystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.Equipment;
import com.xiaowei.worksystem.entity.WorkOrderType;
import com.xiaowei.worksystem.entity.customer.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestWorkOrderDTO {
    public interface UpdateStatus{}

    /**
     * 所属设备
     */
    @ApiModelProperty(value = "所属设备")
    private Equipment equipment;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String linkMan;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String linkPhone;
    /**
     * 故障描述
     */
    @ApiModelProperty(value = "故障描述")
    private String errorDescription;
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
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "申请处理人必填!")
    private SysUser proposer;

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
