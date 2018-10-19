package com.xiaowei.attendancesystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@Table(name = "A_PUNCHRECORDITEM")
@SQLDelete(sql = "update A_PUNCHRECORDITEM set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class PunchRecordItem extends MultiBaseEntity {

    /**
     * 打卡时间
     */
    private Time clockTime;

    /**
     * 到达状态
     */
    private Integer arriveStatus;

    /**
     * 打卡地点
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    private Geometry shape;

    /**
     * 打卡图片
     */
    @Lob
    private String punchFileStore;

    /**
     * 所属打卡记录id
     */
    private String punchRecordId;

    @Transient
    private String offWkt;

    public String getWkt() {
        if (this.shape != null) {
            return this.shape.toText();
        }
        return null;
    }

    @JsonIgnore
    public Geometry getShape() {
        return shape;
    }
}
