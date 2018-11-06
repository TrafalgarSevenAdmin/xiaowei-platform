package com.xiaowei.account.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 系统配置表
 */
@Table(name = "sys_config")
@Entity
@Data
public class SysConfig extends BaseEntity {

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置代码
     */
    private String code;

    /**
     * 配置的值
     */
    @Lob
    private String value;

    /**
     * 配置描述
     */
    private String note;

    /**
     * 前端填写的参数类型
     */
    private String type;

}