package com.xiaowei.worksystem.entity.assets;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品分类
 */
@Data
@Entity
@Table(name = "W_PRO_CLASS")
public class ProClass extends BaseEntity {
    /**
     * 分类编码
     * 该分类唯一对应的一个数字编码
     */
    public String code;

    /**
     * 分类名称
     * 该仓库对应的名称
     */
    public String name;

    /**
     * 分类级别
     * 该分类对应的级别，如三级分类，4级分类等
     */
    public Integer level;

}
