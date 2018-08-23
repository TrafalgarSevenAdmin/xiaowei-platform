package com.xiaowei.flow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.flow.constants.TaskStatus;
import com.xiaowei.flow.entity.auth.AuthGrant;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * 流程启动后的任务实体
 */
@Data
@Builder
@Entity
@Table(name = "wf_task")
@SQLDelete(sql = "update wf_task set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class FlowTask extends BaseEntity {

    @Tolerate
    public FlowTask() {
    }

    /**
     * 任务名称
     */
    String name;

    /**
     * 任务代码
     * 类似 工单代码。
     * 只供展示与记录使用，但也应当满足唯一原则
     */
    String code;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "flow_id")
    FlowDefinition flow;

    /**
     * 运行中配置的抄送用户
     */
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    @Where(clause = "node_id is null")//只查询审核用户
    List<AuthGrant> viewer = new ArrayList<>();

    /**
     * 创建此任务的用户
     */
    private String createUserId;

    /**
     * 创建此任务的用户名称
     */
    private String createUserName;

    /**
     * 任务附加数据
     */
    @Lob
    String ext;

    /**
     * 任务状态
     */
    TaskStatus status;

    /**
     * 最后一次完成的任务
     */
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "now_task_id")
    FlowTaskExecuteHistory nowTaskHistory;

    /**
     * 下一个节点
     * 在完成节点时，此值就应该被设置
     * 若不是被自由驳回，那么此值从 nowTaskHistory-> node ->nextNodes中通过计算分支表达式得到。
     * 最后一个节点时，此节点为空
     */
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "next_node_id")
    FlowNode nextNode;

    /**
     * 最后一次更新时间
     */
    Date updateTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FlowTask that = (FlowTask) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(code, that.code)
                .append(flow, that.flow)
                .append(viewer, that.viewer)
                .append(createUserId, that.createUserId)
                .append(ext, that.ext)
                .append(status, that.status)
                .append(nextNode, that.nextNode)
                .append(updateTime, that.updateTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(code)
                .append(flow)
                .append(viewer)
                .append(createUserId)
                .append(ext)
                .append(status)
                .append(nextNode)
                .append(updateTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "FlowTask{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", viewer=" + viewer +
                ", createUserId='" + createUserId + '\'' +
                ", ext='" + ext + '\'' +
                ", status=" + status +
                ", updateTime=" + updateTime +
                '}';
    }
}