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
    @ApiModelProperty(value = "科目说明")
    private String state;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private Float taxRate;

    /**
     * 计费类型
     */
    @ApiModelProperty(value = "计费类型")
    private Integer costType;
    /**
     * 是否按照城市
     */
    @ApiModelProperty(value = "是否按照城市")
    private Boolean city;
    /**
     * 是否按照城市级别
     */
    @ApiModelProperty(value = "是否按照城市级别")
    private Boolean cityLevel;
    /**
     * 是否按照舱位级别
     */
    @ApiModelProperty(value = "是否按照舱位级别")
    private Boolean shipLevel;
    /**
     * 是否按照岗位级别
     */
    @ApiModelProperty(value = "是否按照岗位级别")
    private Boolean postLevel;
    /**
     * 申请单价限额
     */
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "申请单价限额必填!")
    @ApiModelProperty(value = "申请单价限额")
    private Double requestQuota;
}
