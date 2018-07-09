package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.core.basic.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 工程师处理工单附表实体
 */
@Entity
@Table(name = "W_ENGINEERWORK")
public class EngineerWork extends BaseEntity{
    /**
     * 接单时间
     */
    private Date receivedTime;
    /**
     * 预约时间
     */
    private Date appointTime;
    /**
     * 出发时间
     */
    private Date departeTime;
    /**
     * 开始处理时间
     */
    private Date beginInhandTime;
    /**
     * 完成处理时间
     */
    private Date endInhandTime;
    /**
     * 出发地
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry startShape;
    /**
     * 目的地
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry arriveShape;
    @Transient
    private String startWkt;
    @Transient
    private String arriveWkt;

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Date getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Date appointTime) {
        this.appointTime = appointTime;
    }

    public Date getDeparteTime() {
        return departeTime;
    }

    public void setDeparteTime(Date departeTime) {
        this.departeTime = departeTime;
    }

    public Date getBeginInhandTime() {
        return beginInhandTime;
    }

    public void setBeginInhandTime(Date beginInhandTime) {
        this.beginInhandTime = beginInhandTime;
    }

    public Date getEndInhandTime() {
        return endInhandTime;
    }

    public void setEndInhandTime(Date endInhandTime) {
        this.endInhandTime = endInhandTime;
    }

    public Geometry getStartShape() {
        return startShape;
    }

    public void setStartShape(Geometry startShape) {
        this.startShape = startShape;
    }

    public Geometry getArriveShape() {
        return arriveShape;
    }

    public void setArriveShape(Geometry arriveShape) {
        this.arriveShape = arriveShape;
    }

    public String getStartWkt() {
        return startWkt;
    }

    public void setStartWkt(String startWkt) {
        this.startWkt = startWkt;
    }

    public String getArriveWkt() {
        return arriveWkt;
    }

    public void setArriveWkt(String arriveWkt) {
        this.arriveWkt = arriveWkt;
    }
}
