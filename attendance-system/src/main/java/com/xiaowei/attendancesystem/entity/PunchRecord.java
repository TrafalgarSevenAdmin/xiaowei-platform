package com.xiaowei.attendancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.attendancesystem.status.PunchRecordStatus;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * 打卡记录
 */
@Data
@Entity
@Table(name = "A_PUNCHRECORD")
@SQLDelete(sql = "update A_PUNCHRECORD set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class PunchRecord extends BaseEntity{
    /**
     * 上班打卡时间
     */
    private Time clockInTime;
    /**
     * 上班打卡状态
     */
    private PunchRecordStatus onPunchRecordStatus;
    /**
     * 下班打卡状态
     */
    private PunchRecordStatus offPunchRecordStatus;
    /**
     * 下班打卡时间
     */
    private Time clockOutTime;
    /**
     * 打卡人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "user_id")
    @Fetch(FetchMode.JOIN)
    private SysUser sysUser;
    /**
     * 打卡次数
     */
    private Integer punchCount;
    /**
     * 打卡日期
     */
    @Temporal(TemporalType.DATE)
    private Date punchDate;

    /**
     * 是否请假
     */
    private Boolean vacate;
    /**
     * 请假类型
     */
    private String vacateType;
    /**
     * 上班打卡图片
     */
    @Lob
    private String onPunchFileStore;

    @Transient
    private String punchFileStore;

    /**
     * 下班打卡图片
     */
    @Lob
    private String offPunchFileStore;

    /**
     * 上班打卡地点
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    private Geometry onShape;

    /**
     * 下班打卡地点
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    private Geometry offShape;

    /**
     * 上班距离
     */
    private Double onDistance;

    /**
     * 下班距离
     */
    private Double offDistance;


    @Transient
    private String onWkt;

    @Transient
    private String offWkt;

    public PunchRecord() {
    }

    public String getOnWkt() {
        if (this.onShape != null) {
            return this.onShape.toText();
        }
        return null;
    }

    @JsonIgnore
    public Geometry getOffShape() {
        return offShape;
    }

    @JsonIgnore
    public Geometry getOnShape() {
        return onShape;
    }

    public String getOffWkt() {
        if (this.offShape != null) {
            return this.offShape.toText();
        }
        return null;
    }

    public PunchRecord(SysUser sysUser) {
        this.setCreatedTime(new Date());
        this.sysUser = sysUser;
        this.punchDate = new Date();
        this.punchCount = 0;
        this.onPunchRecordStatus = PunchRecordStatus.CLOCKISNULL;
        this.offPunchRecordStatus = PunchRecordStatus.CLOCKISNULL;
    }

}
