package com.xiaowei.flow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 流程节点定义实体
 */
@Data
@Builder
@Entity
@Table(name = "wf_node_definition")
@SQLDelete(sql = "update wf_node_definition set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class FlowNode extends BaseEntity {

    @Tolerate
    public FlowNode() {
    }

    /**
     * 节点名称
     */
    String name;

    /**
     * 节点代码
     */
    String code;

    /**
     * 节点描述
     */
    String describe;

    /**
     * 业务附属数据
     */
    @Lob
    String ext;

    /**
     * 所属流程
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "flow_id",insertable = false,updatable = false)
    @JsonIgnore
    FlowDefinition flow;

    @Column(name = "flow_id")
    String flowId;


    /**
     * 进入此节点的条件，有以下方案可选
     * 1.js 通用，但是不能调用内部环境与java类，运行较慢
     * 2.spring el  不够通用，能够灵活的使用java类与spring容器中的bean
     * 3.groovy 通用，运行比js快，可以调用java,但不能直接调用spring中的bean，可使用间接方法
     *
     * 用于分支或并行任务、子任务。
     * 一期暂时不考虑
     */
    String enterCondition = "true";

    /**
     * 下个节点，自关联。可能有分支等情况,因此是多个
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "last_node_id", referencedColumnName = "id")
    Set<FlowNode> nextNodes;

    /**
     * 对此节点的操作权限
     */
    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "node_id", referencedColumnName = "id")//可能会吧任务中的权限查出来
    @Where(clause = "task_id is null")//不查询正在运行中的流程
    List<AuthGrant> auth = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FlowNode that = (FlowNode) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(code, that.code)
                .append(describe, that.describe)
                .append(ext, that.ext)
                .append(flowId, that.flowId)
                .append(enterCondition, that.enterCondition)
                .append(auth, that.auth)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(code)
                .append(describe)
                .append(ext)
                .append(flowId)
                .append(enterCondition)
                .append(auth)
                .toHashCode();
    }
}