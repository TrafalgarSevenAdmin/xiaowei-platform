package com.xiaowei.worksystem.entity.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.worksystem.status.WarehouseType;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 仓库档案
 */
@Data
@Entity
@Table(name = "W_WAREHOUSE")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class Warehouse extends MultiBaseEntity {

    /**
     * 仓库编码
     */
    public String code;

    /**
     * 仓库名称
     */
    public String name;

    /**
     * 上级仓库
     * 该仓库对应的上级仓库
     */
    public String parentWarehouse;

    /**
     * 仓库分类
     * 该仓库对应的分类
     * 0:虚拟厂库，1：公司厂库，2：个人库，
     */
    public WarehouseType type;

    /**
     * 仓库地址
     * 该仓库所在地址
     */
    public String addr;

    /**
     * 仓库管理员
     * 该仓库管理员的姓名
     */
    public String ownerName;

    /**
     * 联系电话
     * 该仓库管理员的联系电话
     */
    public String ownerPhone;

    /**
     * 备注
     * 备注信息
     */
    public String note;

    /**
     * 个人厂库所属人信息
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "userId",insertable = false,updatable = false)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private SysUser user;

}
