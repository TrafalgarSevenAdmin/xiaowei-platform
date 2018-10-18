package com.xiaowei.accountweb.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TenementDTO {
    public interface UpdateStatus{}
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "租户编码必填!")
    @ParamField("租户编码")
    private String code;
    /**
     * 租户名称
     */
    @ApiModelProperty(value = "租户名称")
    @ParamField("租户名称")
    private String name;
    /**
     * 状态
     */
    @ApiModelProperty(value = "租户状态:0代表正常,1代表禁用")
    @NotNull(groups = {TenementDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {TenementDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;
}
