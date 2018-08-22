package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.RequestFormItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class RequestFormDTO {
    public interface Audit{}

    /**
     * 所属工单编号
     */
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "所属工单编号必填!")
    @ApiModelProperty(value = "所属工单编号")
    private String workOrderCode;

    /**
     * 填报总计金额
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "填报总计金额必填!")
    @ApiModelProperty(value = "填报总计金额")
    private Double fillAmount;
    /**
     * 审核总计金额
     */
    @NotNull(groups = {RequestFormDTO.Audit.class,V.Update.class},message = "审核总计金额!")
    @ApiModelProperty(value = "审核总计金额")
    private Double trialAmount;

    /**
     * 审核人
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "审核人必填!")
    @ApiModelProperty(value = "审核人")
    private List<SysUser> trials;

    /**
     * 最终审核人
     */
    @NotNull(groups = {Audit.class},message = "最终审核人必填!")
    @ApiModelProperty(value = "审核人")
    private SysUser audit;

    /**
     * 状态
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "状态必填!")
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String option;

    /**
     * 费用申请人
     */
    @ApiModelProperty(value = "费用申请人")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "费用申请人必填!")
    private SysUser requestUser;

    @ApiModelProperty(value = "申请单明细")
    @NotNull(groups = {V.Insert.class,V.Update.class,Audit.class},message = "申请单明细必填!")
    private List<RequestFormItem> requestFormItems;

    /**
     * 审核时间
     */
    private Date auditTime;
}
