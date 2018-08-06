package com.xiaowei.account.entity;

import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * 系统公司表
 */
@Data
@Table(name = "sys_company")
@Entity
public class Company extends BaseEntity {
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 编号
     */
    @Column(updatable = false)
    private String code;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态:0表示正常,1表示停用,99代表删除
     */
    private Integer status;

    /**
     * 公司logo
     */
    private String logo;

    /**
     * 负责人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "principal_id")
    @Fetch(FetchMode.JOIN)
    private SysUser principal;

    /**
     * 简介
     */
    private String intro;

    public String getLogo() {
        return UploadConfigUtils.transIdsToPath(this.logo);
    }
}
