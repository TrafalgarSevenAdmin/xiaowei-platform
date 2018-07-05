package com.xiaowei.worksystem.entity;

import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.status.WorkpieceStoreType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 备品备件
 */
@Entity
@Table(name = "W_WORKPIECE")
public class Workpiece extends BaseEntity{
    /**
     * 工件名称
     */
    private String name;

    /**
     * 工件编号
     */
    private String code;

    /**
     * 所属厂库类型，0个人厂库，1总库
     */
    private Integer storeType = WorkpieceStoreType.USER.getStatus();

    /**
     * 好件数量
     */
    private Integer FineTotal;

    /**
     * 坏件数量
     */
    private Integer BadTotal;

    /**
     * 个人厂库所属人
     */
    private String userId;

    /**
     * 数据状态
     */
    private String status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public Integer getFineTotal() {
        return FineTotal;
    }

    public void setFineTotal(Integer fineTotal) {
        FineTotal = fineTotal;
    }

    public Integer getBadTotal() {
        return BadTotal;
    }

    public void setBadTotal(Integer badTotal) {
        BadTotal = badTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
