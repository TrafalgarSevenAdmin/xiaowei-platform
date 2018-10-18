package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.entity.flow.WorkFlow;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 工单请求
 */
@Entity
@Table(name = "W_REQUESTWORKORDER")
@Data
@SQLDelete(sql = "update W_REQUESTWORKORDER set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class RequestWorkOrder extends MultiBaseEntity {

    /**
     * 流程模板
     */
    @ManyToOne(targetEntity = WorkFlow.class)
    @JoinColumn(name = "workflow_id")
    @Fetch(FetchMode.JOIN)
    private WorkFlow workFlow;

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
     * 要求
     */
    private String demand;
    /**
     * 工单标题
     */
    private String workOrderTitle;
    /**
     * 服务类型
     */
    @ManyToOne(targetEntity = WorkOrderType.class)
    @JoinColumn(name = "type_id")
    @Fetch(FetchMode.JOIN)
    private WorkOrderType workOrderType;
    /**
     * 申请处理人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "proposer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser proposer;

    /**
     * 处理工程师
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "engineer_id")
    @Fetch(FetchMode.JOIN)
    private SysUser engineer;

    /**
     * 报修文件id(多文件以分号隔开)
     */
    @Lob
    private String repairFileStore;

    /**
     * 报修音频id(多文件以分号隔开)
     */
    @Lob
    private String repairVoice;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 服务对象
     */
    @OneToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id")
    @Fetch(FetchMode.JOIN)
    private Customer customer;

    /**
     * 区县
     */
    private String county;

    /**
     * 地图定位
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry shape;

    @Transient
    private String wkt;

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
