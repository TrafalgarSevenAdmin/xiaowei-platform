package com.xiaowei.expensereimbursementweb.dto;

import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShipLevelDTO {
    /**
     * 所属科目
     */
    @ApiModelProperty(value = "所属科目")
    private ExpenseSubject expenseSubject;
    /**
     * 舱位级别
     */
    @ApiModelProperty(value = "舱位级别")
    private String shipLevel;
}
