package com.xiaowei.attendancesystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PunchRecordDTO {

    /**
     * 打卡人
     */
    @ApiModelProperty(value = "打卡人")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "打卡人必填!")
    private SysUser sysUser;
    /**
     * 打卡地点的wkt字符串
     */
    @ApiModelProperty(value = "打卡地点的wkt字符串")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "打卡地点的wkt字符串!")
    private String wkt;
}
