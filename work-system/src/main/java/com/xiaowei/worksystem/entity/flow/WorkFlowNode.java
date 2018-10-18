package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;

@Data
@Entity
@Table(name = "W_WORKFLOWNODE")
public class WorkFlowNode extends MultiBaseEntity {
    private String code;
    /**
     * 是否收费
     */
    private Boolean charge;
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
     * 工作标准图片
     */
    private String standardFileStore;
    /**
     * 保内收费
     */
    private Double toll;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 保外收费
     */
    private Double outToll;

    /**
     * 是否需要审核
     */
    private Boolean audit;
    /**
     * 内部价格
     */
    private Double insidePrice;
    /**
     * 外部价格
     */
    private Double outsidePrice;
    /**
     * 预计时长
     */
    private Time predictTime;
    /**
     * 版本号
     */
    private String version;
}
