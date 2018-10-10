package com.xiaowei.attendancesystem.entity;

import com.xiaowei.account.entity.SysUser;
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
@SQLDelete(sql = "update sys_user set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class PunchRecord extends BaseEntity{
    /**
     * 上班打卡时间
     */
    private Time clockInTime;
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
     * 是否迟到
     */
    private Boolean beLate;
    /**
     * 是否早退
     */
    private Boolean leaveEarly;
    /**
     * 是否请假
     */
    private Boolean vacate;
    /**
     * 请假类型
     */
    private String vacateType;
    /**
     * 打卡图片
     */
    private String punchFileStore;

    public PunchRecord() {
    }

    public PunchRecord(SysUser sysUser) {
        this.setCreatedTime(new Date());
        this.sysUser = sysUser;
        this.punchDate = new Date();
        this.punchCount = 0;
        this.beLate = false;
    }
}
