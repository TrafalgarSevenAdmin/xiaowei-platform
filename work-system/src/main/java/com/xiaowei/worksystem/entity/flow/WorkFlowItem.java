package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;

/**
 * 模板流程明细
 */
@Data
@Entity
@Table(name = "W_WORKFLOWITEM")
public class WorkFlowItem extends BaseEntity{
    /**
     * 是否收费
     */
    private Boolean isCharge;
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
     * 收费
     */
    private Double toll;

    /**
     * 是否需要审核
     */
    private Boolean audit;

    /**
     * 预计时长
     */
    private Time predictTime;
    /**
     * 版本号
     */
    private String version;
    /**
     * 维修顺序
     */
    private Integer orderNumber;
    /**
     * 所属模板
     */
    private String workFlowId;
}
