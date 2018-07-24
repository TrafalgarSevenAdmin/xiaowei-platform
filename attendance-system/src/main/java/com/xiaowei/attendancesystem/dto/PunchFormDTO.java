package com.xiaowei.attendancesystem.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 该类为查询报表的前端视图对象
 */
@Data
public class PunchFormDTO {

    @ApiModelProperty(value = "公司id")
    @NotBlank(message = "公司id必填!")
    private String companyId;

    @ApiModelProperty(value = "选择月份")
    @NotNull(message = "选择月份必填!")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date selectMonth;
}
