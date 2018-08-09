package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * 工单实体
 */
@Entity
@Table(name = "W_WORKORDER")
@Data
public class WorkOrder extends BaseEntity {

    /**
     * 工单编号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 所属设备
     */
    @ManyToOne(targetEntity = Equipment.class)
    @JoinColumn(name = "equipment_id")
    @Fetch(FetchMode.JOIN)
    private Equipment equipment;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 故障描述
     */
    private String errorDescription;
    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 工单类型
     */
    private String workOrderType;

    /**
     * 针对后台处理人员状态
     */
    private Integer systemStatus;
    /**
     * 针对用户状态
     */
    private Integer userStatus;
    /**
     * 申请处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "proposer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser proposer;

    /**
     * 后台处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "backgrounder_id")
    @Fetch(FetchMode.JOIN)
    private SysUser backgrounder;
    /**
     * 处理工程师
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "engineer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser engineer;

    /**
     * 当前处理步骤
     */
    private Integer currentOrderNumber;

    /**
     * 用户评价
     */
    @OneToOne(targetEntity = Evaluate.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluate_id")
    @Fetch(FetchMode.JOIN)
    private Evaluate evaluate;

    /**
     * 工程师处理工单附表
     */
    @OneToOne(targetEntity = EngineerWork.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "engineerWork_id")
    @Fetch(FetchMode.JOIN)
    private EngineerWork engineerWork;

    /**
     * 报修文件id(多文件以分号隔开)
     */
    private String repairFileStore;

    /**
     * 地图定位
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry shape;

    @Transient
    private String wkt;

    public String getRepairFileStore() {
        return UploadConfigUtils.transIdsToPath(this.repairFileStore);
    }

    public String getWkt() {
        if (this.shape != null) {
            return this.shape.toText();
        }
        return wkt;
    }

    @JsonIgnore
    public Geometry getShape() {
        return shape;
    }
}
