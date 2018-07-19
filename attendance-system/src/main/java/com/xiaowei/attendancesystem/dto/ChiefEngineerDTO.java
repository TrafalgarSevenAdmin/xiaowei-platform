package com.xiaowei.attendancesystem.dto;

import com.xiaowei.account.entity.Department;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.List;

@Data
public class ChiefEngineerDTO {
    public interface UpdateStatus{}
    /**
     * 办公点类型
     */
    @ApiModelProperty(value = "办公点类型")
    private String type;
    /**
     * 办公点名称
     */
    @ApiModelProperty(value = "办公点名称")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "办公点名称必填!")
    private String chiefName;
    /**
     * 办公点地址
     */
    @ApiModelProperty(value = "办公点地址")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "办公点地址必填!")
    private String address;
    /**
     * 上班打卡开始时间
     */
    @ApiModelProperty(value = "上班打卡开始时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "上班打卡开始时间必填!")
    private Time beginClockInTime;
    /**
     * 上班打卡结束时间
     */
    @ApiModelProperty(value = "上班打卡结束时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "上班打卡结束时间必填!")
    private Time endClockInTime;
    /**
     * 迟到时间
     */
    @ApiModelProperty(value = "迟到时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "迟到时间必填!")
    private Time beLateTime;
    /**
     * 下班打卡开始时间
     */
    @ApiModelProperty(value = "下班打卡开始时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "下班打卡开始时间必填!")
    private Time beginClockOutTime;
    /**
     * 下班打卡结束时间
     */
    @ApiModelProperty(value = "下班打卡结束时间")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "下班打卡结束时间必填!")
    private Time endClockOutTime;

    @ApiModelProperty(value = "办公点的wkt字符串")
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "办公点的wkt字符串必填!")
    private String wkt;

    @ApiModelProperty(value = "办公点状态:0代表正常,1代表禁用")
    @NotNull(groups = {ChiefEngineerDTO.UpdateStatus.class},message = "状态不能为空!")
    @Range(min = 0,max = 1,groups = {ChiefEngineerDTO.UpdateStatus.class},message = "传入状态为非法状态!")
    private Integer status;

    @ApiModelProperty(value = "办公点下的部门")
    private List<Department> departments;

}
