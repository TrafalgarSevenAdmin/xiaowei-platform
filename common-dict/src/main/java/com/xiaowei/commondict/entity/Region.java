package com.xiaowei.commondict.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 行政区域实体
 */
@Data
@Table(name = "SYS_REGION")
@Entity
public class Region extends BaseEntity {

    //行政区完整代码
    @Column(name = "code",length = 20,nullable = false,unique = true)
    private String code;

    //行政区代码
    @Column(name = "short_code",length = 20,nullable = false,unique = true)
    private String shortCode;

    //行政区等级
    @Column(name = "level",length = 20,nullable = false)
    private Integer level;

    //行政区名称
    @Column(name = "name",length = 50,nullable = false)
    private String name;

    //全称
    @Column(name = "mergerName",length = 150,nullable = true)
    private String mergerName;

    //父级行政区短代码
    @Column(name = "parentShortCode",length = 20)
    private String parentShortCode;
}
