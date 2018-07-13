package com.xiaowei.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * 系统部门表
 */
@Data
@Table(name = "SYS_DEPARTMENT")
@Entity
public class Department extends BaseEntity {
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

    /**
     * 部门下的员工
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "departments")
    @JsonIgnore
    private List<SysUser> users;

    @Transient
    private String logoPath;

}
