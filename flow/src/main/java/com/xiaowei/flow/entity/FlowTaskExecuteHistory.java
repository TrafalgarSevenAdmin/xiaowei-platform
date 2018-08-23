package com.xiaowei.flow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.flow.constants.TaskActionType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

/**
 * 任务执行情况记录
 */
@Builder
@Data
@Entity
@Table(name = "wf_task_execute_history")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class FlowTaskExecuteHistory extends BaseEntity {

    @Tolerate
    public FlowTaskExecuteHistory() {}

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    FlowTask task;

    /**
     * 操作历史业务数据
     * 比如此节点被驳回，并记录了驳回原因等
     */
    @Lob
    String ext;

    /**
     * 执行状态
     */
    TaskActionType action = TaskActionType.NORMAL;

    /**
     * 此任务记录的操作用户
     */
    String operationUserId;

    /**
     * 此任务记录的操作用户的名称
     */
    String operationUserName;

    /**
     * 驳回/同意理由
     */
    @Lob
    String reason;

    /**
     * 上一个任务
     * 一般为了获取上一个任务的业务数据
     */
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "last_task_execute_id")
    FlowTaskExecuteHistory lastTask;

    /**
     * 所属节点
     */
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "node_id")
    FlowNode node;

}