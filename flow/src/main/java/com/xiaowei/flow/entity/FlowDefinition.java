package com.xiaowei.flow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.flow.entity.auth.AuthGrant;
import lombok.*;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 流程定义
 */
@Builder
@Data
@Entity
@Table(name = "wf_definition")
@SQLDelete(sql = "update wf_definition set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
@JsonIgnoreProperties(value = {"delete_flag", "delete_time"})
public class FlowDefinition extends BaseEntity {

    @Tolerate
    public FlowDefinition() {}

    /**
     * 流程中文名称
     */
    String name;

    /**
     * 流程代码,必须唯一
     */
    @Column(unique = true)
    String code;

    /**
     * 流程描述
     */
    String describe;

    /**
     * 业务附属数据
     */
    @Lob
    String ext;

    /**
     * 入口节点
     */
    @OneToOne(cascade = {CascadeType.ALL})
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "start_node_id")
    FlowNode start;

    /**
     * 抄送人员
     */
    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "flow_id",referencedColumnName = "id")
    @Where(clause = "node_id is null")
    Set<AuthGrant> viewer = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FlowDefinition flow = (FlowDefinition) o;

        return new EqualsBuilder()
                .append(name, flow.name)
                .append(code, flow.code)
                .append(describe, flow.describe)
                .append(ext, flow.ext)
                .append(start, flow.start)
                .append(viewer, flow.viewer)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(code)
                .append(describe)
                .append(ext)
                .append(start)
                .append(viewer)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "FlowDefinition{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", describe='" + describe + '\'' +
                ", ext='" + ext + '\'' +
                ", viewer=" + viewer +
                '}';
    }
}