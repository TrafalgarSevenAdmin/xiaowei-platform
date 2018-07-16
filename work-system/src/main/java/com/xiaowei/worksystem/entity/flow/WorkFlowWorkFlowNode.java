package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "W_WORKFLOW_WORKFLOWNODE")
public class WorkFlowWorkFlowNode extends BaseEntity{
    /**
     * 流程模板id
     */
    private String flowId;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 维修顺序
     */
    private Integer orderNumber;

}
