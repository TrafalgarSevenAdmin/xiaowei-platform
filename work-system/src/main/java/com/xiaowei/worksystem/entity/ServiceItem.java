package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
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
@Data
@Entity
@Table(name = "W_SERVICEITEM")
public class ServiceItem extends BaseEntity{
    /**
     * 是否收费
     */
    private Boolean charge;
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
     * 工作标准说明
     */
    private String standard;
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

    /**
     * 版本号
     */
    private String version;


}
