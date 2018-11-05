package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeamItem;
import com.xiaowei.expensereimbursement.status.AuditExpenseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AuditExpenseTeamDTO {
    /**
     * 小组名称
     */
    @ApiModelProperty(value = "小组名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "小组名称必填!")
    @ParamField("小组名称")
    private String teamName;
    /**
     * 小组审核类型
     */
    @ApiModelProperty(value = "小组审核类型")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "小组审核类型必填!")
    @ParamField("小组审核类型")
    private AuditExpenseType auditExpenseType;
    /**
     * 小组明细
     */
    @ApiModelProperty(value = "小组明细")
    private List<AuditExpenseTeamItem> auditExpenseTeamItems;
}
