package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "E_EXPENSESUBJECT")
@Entity
@Data
public class ReimbursementStandard extends BaseEntity {
    /**
     * 费用科目编号
     */
    private String subjectCode;
    /**
     * 舱位级别
     */
    private String shipLevel;
    /**
     * 出行方式
     */
    private String outType;
    /**
     * 城市级别
     */
    private String cityLevel;
    /**
     * 岗位级别
     */
    private String postLevel;
    /**
     * 出发城市
     */
    private String startCity;
    /**
     * 到达城市
     */
    private String endCity;
    /**
     * 计费类型
     */
    private Integer costType;
    /**
     * 单价
     */
    private Double unitCost;
}
