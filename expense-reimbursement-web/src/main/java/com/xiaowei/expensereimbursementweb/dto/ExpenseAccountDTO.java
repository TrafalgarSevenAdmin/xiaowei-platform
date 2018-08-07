package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.bean.AccountContentBean;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExpenseAccountDTO {
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "科目名称必填!")
    @ApiModelProperty(value = "科目名称")
    private String accountName;

    @ApiModelProperty(value = "科目内容")
    private List<AccountContentBean> accountContentBeans = new ArrayList<>();

    /**
     * 科目说明
     */
    @ApiModelProperty(value = "科目说明")
    private String state;

    /**
     * 所属一级科目
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "所属一级科目必填!")
    @ApiModelProperty(value = "所属一级科目")
    private ExpenseSubject expenseSubject;

    @ApiModelProperty(value = "税率")
    private Float taxRate;
}
