package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
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
    @ParamField("科目名称")
    private String subjectName;

    /**
     * 父级科目id
     */
    @ApiModelProperty(value = "父级科目id")
    @ParamField("父级科目id")
    private String parentId;

    @ApiModelProperty(value = "科目内容")
    @ParamField("科目内容")
    private List<AccountContentBean> accountContentBeans = new ArrayList<>();

    /**
     * 科目说明
     */
    @ApiModelProperty(value = "科目说明")
    @ParamField("科目说明")
    private String state;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    @ParamField("税率")
    private Float taxRate;

    /**
     * 计费类型
     */
    @ApiModelProperty(value = "计费类型")
    @ParamField("计费类型")
    private Integer costType;
    /**
     * 是否按照城市
     */
    @ApiModelProperty(value = "是否按照城市")
    @ParamField("是否按照城市")
    private Boolean city;
    /**
     * 是否按照城市级别
     */
    @ApiModelProperty(value = "是否按照城市级别")
    @ParamField("是否按照城市级别")
    private Boolean cityLevel;
    /**
     * 是否按照舱位级别
     */
    @ApiModelProperty(value = "是否按照舱位级别")
    @ParamField("是否按照舱位级别")
    private Boolean shipLevel;
    /**
     * 是否按照岗位级别
     */
    @ApiModelProperty(value = "是否按照岗位级别")
    @ParamField("是否按照岗位级别")
    private Boolean postLevel;
    /**
     * 科目最大报销数量
     */
    private Integer maxCount;
}
