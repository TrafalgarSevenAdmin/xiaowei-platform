package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 流程模板
 */
@Data
@Entity
@Table(name = "W_WORKFLOW")
public class WorkFlow extends BaseEntity {
    /**
     * 模板名称
     */
    private String workFlowName;
    /**
     * 模板编号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 模板类型
     */
    private String type;
    /**
     * 模板简介
     */
    private String intro;

    /**
     * 模板下的流程明细
     */
    @Transient
    private List<WorkFlowItem> workFlowItems;

}
