package com.xiaowei.commondict.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "SYS_DICTIONARY")
@Entity
public class Dictionary extends BaseEntity{
    /**
     * 字典的完整编码
     */
    @Column(unique = true)
    private String code;
    /**
     * 字典独立编码
     */
    private String ownCode;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典等级
     */
    private Integer level;
    /**
     * 父级id
     */
    private String parentId;
}
