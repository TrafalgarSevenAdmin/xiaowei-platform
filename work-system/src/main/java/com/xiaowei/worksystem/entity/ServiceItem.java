package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;

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
    private Integer orderNumber;
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
     * 状态
     */
    private Integer status;

    /**
     * 所属工单
     */
    @ManyToOne(targetEntity = WorkOrder.class)
    @JoinColumn(name = "workOrder_id")
    @Fetch(FetchMode.JOIN)
    private WorkOrder workOrder;

    /**
     * 收费
     */
    private Double toll;

    /**
     * 是否需要审核
     */
    private Boolean audit;
    /**
     * 开始处理时间
     */
    private Date beginTime;
    /**
     * 结束处理时间
     */
    private Date endTime;

    /**
     * 预计时长
     */
    private Time predictTime;

    public Time getPredictTime() {
        return predictTime;
    }

    public void setPredictTime(Time predictTime) {
        this.predictTime = predictTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getAudit() {
        return audit;
    }

    public void setAudit(Boolean audit) {
        this.audit = audit;
    }

    public Boolean getCharge() {
        return isCharge;
    }

    public void setCharge(Boolean charge) {
        isCharge = charge;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public Double getToll() {
        return toll;
    }

    public void setToll(Double toll) {
        this.toll = toll;
    }
}
