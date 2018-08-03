package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 审核配置实体
 */
@Table(name = "E_AUDITCONFIGURATION")
@Entity
@Data
public class AuditConfiguration extends BaseEntity {
    /**
     * 审核配置用户
     */
    private String userId;
    /**
     * 审核配置部门
     */
    private String departmentId;
    /**
     * 审核配置类型状态
     */
    private Integer typeStatus;

}
