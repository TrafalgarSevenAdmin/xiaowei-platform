package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.core.validate.V;
import com.xiaowei.expensereimbursement.bean.AccountContentBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExpenseSubjectDTO {
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "科目名称必填!")
    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    /**
     * 父级科目id
     */
    @ApiModelProperty(value = "父级科目id")
    private String parentId;

    @ApiModelProperty(value = "科目内容")
    private List<AccountContentBean> accountContentBeans = new ArrayList<>();

    /**
     * 科目说明
     */
    private String state;

    /**
     * 税率
     */
    private Float taxRate;
}
