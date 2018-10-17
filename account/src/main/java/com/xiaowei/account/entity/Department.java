package com.xiaowei.account.entity;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 系统部门表
 */
@Data
@Table(name = "SYS_DEPARTMENT")
@Entity
@SQLDelete(sql = "update SYS_DEPARTMENT set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Department extends MultiBaseEntity {
    /**
     * 编号
     */
    @Column(updatable = false)
    private String code;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 状态:0表示正常,1表示停用,99代表删除
     */
    private Integer status;
    /**
     * 部门logo
     */
    @Lob
    private String logo;
    /**
     * 简介
     */
    private String intro;
    /**
     * 所属公司
     */
    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "company_id")
    @Fetch(FetchMode.JOIN)
    private Company company;

}
