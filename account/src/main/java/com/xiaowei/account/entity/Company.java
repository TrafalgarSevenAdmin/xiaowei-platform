package com.xiaowei.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

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
     * 公司下的员工
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "companies")
    @JsonIgnore
    private List<SysUser> users;

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

    @Transient
    private String logoPath;

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public SysUser getPrincipal() {
        return principal;
    }

    public void setPrincipal(SysUser principal) {
        this.principal = principal;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
