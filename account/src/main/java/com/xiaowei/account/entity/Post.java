package com.xiaowei.account.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * 岗位
 */
@Data
@Table(name = "SYS_POST")
@Entity
public class Post extends BaseEntity {
    /**
     * 编号
     */
    @Column(updatable = false)
    private String code;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位状态
     */
    private Integer status;
    /**
     * 简介
     */
    private String intro;
    /**
     * 岗位级别
     */
    private Integer level;
    /**
     * 所属公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;
}
