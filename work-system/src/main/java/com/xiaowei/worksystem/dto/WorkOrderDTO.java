package com.xiaowei.worksystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.Equipment;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class WorkOrderDTO {
    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "工单编号必填!")
    private String code;
    /**
     * 所属设备
     */
    @ApiModelProperty(value = "所属设备")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "所属设备必填!")
    private Equipment equipment;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "联系人必填!")
    private String linkMan;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "联系电话必填!")
    @Pattern(regexp = "\\d{11}",groups = {V.Insert.class,V.Update.class},message = "手机号11位!")
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
    private String serviceType;

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
     * 针对工程师状态
     */
    @ApiModelProperty(value = "针对工程师状态")
    private Integer engineerStatus;
    /**
     * 创建方式
     */
    @ApiModelProperty(value = "创建方式,0代表用户创建,1代表后台工作人员创建,2代表自动创建")
    private Integer createdType;
    /**
     * 申请处理人
     */
    @ApiModelProperty(value = "申请处理人")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "申请处理人必填!")
    private SysUser proposer;
    /**
     * 后台处理人
     */
    @ApiModelProperty(value = "后台处理人")
    private SysUser backgrounder;
    /**
     * 处理工程师
     */
    @ApiModelProperty(value = "处理工程师")
    private SysUser engineer;

    @ApiModelProperty(value = "创建时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "创建时间必填!")
    private Date createdTime;

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(Integer systemStatus) {
        this.systemStatus = systemStatus;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getEngineerStatus() {
        return engineerStatus;
    }

    public void setEngineerStatus(Integer engineerStatus) {
        this.engineerStatus = engineerStatus;
    }

    public Integer getCreatedType() {
        return createdType;
    }

    public void setCreatedType(Integer createdType) {
        this.createdType = createdType;
    }

    public SysUser getProposer() {
        return proposer;
    }

    public void setProposer(SysUser proposer) {
        this.proposer = proposer;
    }

    public SysUser getBackgrounder() {
        return backgrounder;
    }

    public void setBackgrounder(SysUser backgrounder) {
        this.backgrounder = backgrounder;
    }

    public SysUser getEngineer() {
        return engineer;
    }

    public void setEngineer(SysUser engineer) {
        this.engineer = engineer;
    }
}
