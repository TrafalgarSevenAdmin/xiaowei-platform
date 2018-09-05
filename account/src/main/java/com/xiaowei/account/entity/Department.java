package com.xiaowei.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * 系统部门表
 */
@Data
@Table(name = "SYS_DEPARTMENT")
@Entity
@SQLDelete(sql = "update SYS_DEPARTMENT set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
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

    @Transient
    @JsonIgnore
    private List<Map<String, String>> logoPath;

    public List<Map<String, String>> getLogoPath() {
        return UploadConfigUtils.transIdsToPath(this.logo);
    }
}
