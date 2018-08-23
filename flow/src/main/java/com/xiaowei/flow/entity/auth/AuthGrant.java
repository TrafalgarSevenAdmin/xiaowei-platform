package com.xiaowei.flow.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 授权列表
 */
@Builder
@Data
@Entity
@Table(name = "wf_auth_grant")
//由于前端展示不需要这些字段
@JsonIgnoreProperties(value = {"delete_flag", "delete_time", "createdTime"})
public class AuthGrant extends BaseEntity {
    @Tolerate
    public AuthGrant() {}

    /**
     * 节点配置用户
     */
    String userId;

    /**
     * 节点配置角色
     */
    String roleId;

    /**
     * 节点配置部门
     */
    String departmentId;

    /**
     * 所属任务
     */
    @Column(name = "task_id")
    @JsonIgnore
    String taskId;

    /**
     * 所属节点
     * 若任务存在，那么此含义为，此任务中此节点的权限
     * 若流程存在，那么此含义为，此流程中配置的此节点的权限
     * 若此值不存在，且
     * 任务存在，那么此含义为，此任务可看到的人的权限
     * 流程存在，那么此含义为，此流程默认可看到的人的权限
     */
    @Column(name = "node_id")
    @JsonIgnore
    String nodeId;

    /**
     * 所属流程
     */
    @Column(name = "flow_id")
    @JsonIgnore
    String flowId;

}