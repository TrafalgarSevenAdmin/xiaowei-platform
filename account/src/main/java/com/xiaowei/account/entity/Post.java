package com.xiaowei.account.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 岗位
 */
@Data
@Table(name = "SYS_POST")
@Entity
@SQLDelete(sql = "update SYS_POST set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Post extends MultiBaseEntity {
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
    private String level;
    /**
     * 所属公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;
}
