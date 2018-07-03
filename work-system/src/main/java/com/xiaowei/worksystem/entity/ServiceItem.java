package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 维修项目实体
 */
@Entity
@Table(name = "W_SERVICEITEM")
public class ServiceItem extends BaseEntity{
    /**
     * 是否收费
     */
    private Boolean isCharge;
    /**
     * 维修顺序
     */
    private Integer order;
    /**
     * 项目创建来源
     */
    private Integer source;
    /**
     * 维修类型
     */
    private String serviceType;
    /**
     * 维修简介
     */
    private String serviceIntro;
    /**
     * 收费
     */
    private Double charge;
    /**
     * 状态
     */
    private Integer status;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getCharge() {
        return isCharge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCharge(Boolean charge) {
        isCharge = charge;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceIntro() {
        return serviceIntro;
    }

    public void setServiceIntro(String serviceIntro) {
        this.serviceIntro = serviceIntro;
    }
}
