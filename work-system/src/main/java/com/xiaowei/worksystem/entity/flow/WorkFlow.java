package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工单流程模板
 */
@Data
@Entity
@Table(name = "W_WORKFLOW")
public class WorkFlow extends BaseEntity {
    private String workFlowName;
    private String code;
    private String type;
    private String intro;
    private Integer status;

}
