package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 舱位级别
 */
@Table(name = "E_SHIPLEVEL")
@Entity
@Data
public class ShipLevel extends BaseEntity {
    /**
     * 所属科目编号
     */
    private String subjectCode;
    /**
     * 舱位级别
     */
    private String shipLevel;
}
