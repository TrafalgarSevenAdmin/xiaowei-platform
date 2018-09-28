package com.xiaowei.worksystem.dto;

import com.xiaowei.commonlog4j.annotation.ParamField;
import com.xiaowei.core.validate.V;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AppointingWorkOrderDTO {
    /**
     * 预约时间
     */
    @NotNull(groups = {V.Insert.class,V.Update.class},message = "预约时间必填!")
    @ParamField("预约时间")
    private Date  appointingTime;
}
