package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.ExpenseFormItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExpenseFormDTO {
    public interface FirstAudit{}
    public interface SecondAudit{}
    /**
     * 所属工单编号
     */
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "所属工单编号必填!")
    @ApiModelProperty(value = "所属工单编号")
    @ParamField("所属工单编号")
    private String workOrderCode;
    /**
     * 所属申请单
     */
    @ParamField("所属申请单")
    @ApiModelProperty(value = "所属申请单")
    private String requestFormCodes;
    /**
     * 报销单照片
     */
    private String formFileStore;
    /**
     * 填报总计金额
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "填报总计金额必填!")
    @ApiModelProperty(value = "填报总计金额")
    @ParamField("填报总计金额")
    private Double fillAmount;
    /**
     * 初审总计金额
     */
    @NotNull(groups = {FirstAudit.class},message = "初审总计金额必填!")
    @ParamField("初审总计金额")
    private Double firstTrialAmount;
    /**
     * 复审总计金额
     */
    @NotNull(groups = {SecondAudit.class},message = "初审总计金额必填!")
    @ParamField("复审总计金额")
    private Double secondTrialAmount;

    /**
     * 初审人
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "初审人必填!")
    @ApiModelProperty(value = "初审人")
    @ParamField("初审人")
    private List<SysUser> firstTrials;

    /**
     * 复审人
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "复审人必填!")
    @ApiModelProperty(value = "复审人")
    @ParamField("复审人")
    private List<SysUser> secondTrials;

    /**
     * 状态
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "状态必填!")
    @ApiModelProperty(value = "状态")
    @ParamField("状态")
    private Integer status;

    /**
     * 初审审批意见
     */
    @ApiModelProperty(value = "初审审批意见")
    @ParamField("初审审批意见")
    private String firstOption;
    /**
     * 复审审批意见
     */
    @ApiModelProperty(value = "复审审批意见")
    @ParamField("复审审批意见")
    private String secondOption;

    @ApiModelProperty(value = "报销单明细")
    @NotNull(groups = {V.Insert.class,V.Update.class,FirstAudit.class,SecondAudit.class},message = "报销单明细必填!")
    @ParamField("报销单明细")
    private List<ExpenseFormItem> expenseFormItems;

    /**
     * 报销人
     */
    @ApiModelProperty(value = "报销人")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "报销人必填!")
    @ParamField("报销人")
    private SysUser expenseUser;

    /**
     * 最终初审人
     */
    @ApiModelProperty(value = "最终初审人")
    @NotNull(groups = {FirstAudit.class},message = "最终初审人必填!")
    @ParamField("最终初审人")
    private SysUser firstAudit;

    /**
     * 最终复审人
     */
    @ApiModelProperty(value = "最终复审人")
    @NotNull(groups = {SecondAudit.class},message = "最终复审人必填!")
    @ParamField("最终复审人")
    private SysUser secondAudit;

}
