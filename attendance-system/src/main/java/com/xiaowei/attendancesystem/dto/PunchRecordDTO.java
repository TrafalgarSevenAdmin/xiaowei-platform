package com.xiaowei.attendancesystem.dto;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.attendancesystem.status.PunchRecordStatus;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PunchRecordDTO {
    public interface UpdateOnStatus{}
    public interface UpdateOffStatus{}
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

    /**
     * 打卡图片
     */
    @ApiModelProperty(value = "打卡图片")
    private String punchFileStore;
    /**
     * 上班打卡状态
     */
    @NotNull(groups = {PunchRecordDTO.UpdateOnStatus.class},message = "上班打卡状态")
    private PunchRecordStatus onPunchRecordStatus;
    /**
     * 下班打卡状态
     */
    @NotNull(groups = {PunchRecordDTO.UpdateOffStatus.class},message = "下班打卡状态")
    private PunchRecordStatus offPunchRecordStatus;
}
