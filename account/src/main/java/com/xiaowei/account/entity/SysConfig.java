package com.xiaowei.account.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统配置表
 */
@Table(name = "sys_permission")
@Entity
@Data
public class SysConfig extends BaseEntity {
    private String name;
    private String value;
    private String systemId;
}
