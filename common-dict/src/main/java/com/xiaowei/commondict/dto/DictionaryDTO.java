package com.xiaowei.commondict.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DictionaryDTO {
    /**
     * 字典独立编码
     */
    @ApiModelProperty(value = "字典独立编码")
    @NotBlank(groups = {V.Insert.class, V.Update.class}, message = "字典独立编码必填!")
    private String ownCode;
    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    @NotBlank(groups = {V.Insert.class, V.Update.class}, message = "字典名称必填!")
    private String name;
    /**
     * 父级id
     */
    private String parentId;
}
