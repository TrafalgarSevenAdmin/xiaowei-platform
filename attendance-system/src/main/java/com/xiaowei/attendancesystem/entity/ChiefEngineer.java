package com.xiaowei.attendancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.entity.Department;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

/**
 * 办公点实体
 */
@Data
@Entity
@Table(name = "A_CHIEFENGINEER")
@SQLDelete(sql = "update A_CHIEFENGINEER set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class ChiefEngineer extends BaseEntity {
    /**
     * 办公点编号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 办公点类型
     */
    private String type;
    /**
     * 办公点名称
     */
    private String chiefName;
    /**
     * 办公点状态
     */
    private Integer status;
    /**
     * 办公点图斑
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    private Geometry shape;
    /**
     * 办公点地址
     */
    private String address;
    /**
     * 上班打卡开始时间
     */
    private Time beginClockInTime;
    /**
     * 上班打卡结束时间
     */
    private Time endClockInTime;
    /**
     * 迟到时间
     */
    private Time beLateTime;
    /**
     * 下班打卡开始时间
     */
    private Time beginClockOutTime;
    /**
     * 下班打卡结束时间
     */
    private Time endClockOutTime;

    /**
     * 办公点打卡距离(单位米)
     */
    private Integer distance;

    /**
     * 办公点下的部门
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="A_CHIEF_DEPARTMENT",
            joinColumns={@JoinColumn(name="chief_id")},
            inverseJoinColumns={@JoinColumn(name="department_id")})
    @JsonIgnore
    private List<Department> departments;

    @Transient
    private String wkt;

    @JsonIgnore
    public Geometry getShape() {
        return shape;
    }

    public void setShape(Geometry shape) {
        this.shape = shape;
    }

    public String getWkt() {
        if (this.shape != null) {
            return this.shape.toText();
        }
        return null;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }
}
