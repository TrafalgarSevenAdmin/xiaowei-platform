package com.xiaowei.wechat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 绑定手机号
 */
@Data
public class BindMobileDTO {

    @ApiModelProperty(value = "姓名")
    String name;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号必填!")
    String mobile;
}
