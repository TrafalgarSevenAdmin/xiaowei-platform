package com.xiaowei.worksystem.entity.flow;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "update W_WORKFLOW set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class WorkFlow extends MultiBaseEntity {
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
     * 状态:0表示正常,1表示停用
     */
    private Integer status;
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
