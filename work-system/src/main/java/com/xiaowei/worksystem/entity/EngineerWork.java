package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

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
@Data
public class EngineerWork extends BaseEntity {
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
    /**
     * 到达状态
     */
    private Integer arriveStatus;
    /**
     * 到达图片
     */
    private String arriveFileStore;

    @Transient
    private String startWkt;
    @Transient
    private String arriveWkt;

    public String getArriveFileStore() {
        return UploadConfigUtils.transIdsToPath(this.arriveFileStore);
    }

    @JsonIgnore
    public Geometry getStartShape() {
        return startShape;
    }

    @JsonIgnore
    public Geometry getArriveShape() {
        return arriveShape;
    }

    public String getStartWkt() {
        if (this.startShape != null) {
            return this.startShape.toText();
        }
        return startWkt;
    }

    public String getArriveWkt() {
        if (this.arriveShape != null) {
            return this.arriveShape.toText();
        }
        return arriveWkt;
    }
}
