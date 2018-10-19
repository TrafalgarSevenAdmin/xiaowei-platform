package com.xiaowei.attendancesystem.dto;

import com.xiaowei.core.validate.V;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Data
public class PunchRecordItemDTO {

    /**
     * 到达状态
     */
    private Integer arriveStatus;

    /**
     * 打卡地点
     */
    private String wkt;

    /**
     * 打卡图片
     */
    @Lob
    private String punchFileStore;

    @ApiModelProperty(value = "打卡人id")
    @NotBlank(groups = {V.Insert.class,V.Update.class},message = "打卡人id必填!")
    private String userId;

}
