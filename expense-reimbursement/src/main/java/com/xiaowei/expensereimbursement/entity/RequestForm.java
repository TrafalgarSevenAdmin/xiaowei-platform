package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 费用申请单
 */
@Table(name = "E_REQUESTFORM")
@Entity
@Data
@SQLDelete(sql = "update E_REQUESTFORM set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class RequestForm extends BaseEntity {
    /**
     * 所属工单编号
     */
    private String workOrderCode;
    /**
     * 申请单编号
     */
    @Column(unique = true,updatable = false)
    private String code;

    /**
     * 填报总计金额
     */
    private Double fillAmount;
    /**
     * 审核总计金额
     */
    private Double trialAmount;

    /**
     * 驳回次数
     */
    private Integer turnDownCount;

    /**
     * 审核人
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="e_requestform_trial",
            joinColumns={@JoinColumn(name="REQUESTFORM_ID")},
            inverseJoinColumns={@JoinColumn(name="TRIAL_ID")})
    @JsonIgnore
    private List<SysUser> trials;

    /**
     * 最终审核人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "auditId")
    @Fetch(FetchMode.JOIN)
    private SysUser audit;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 审批意见
     */
    private String option;

    /**
     * 费用申请人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "requestUserId")
    @Fetch(FetchMode.JOIN)
    private SysUser requestUser;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "requestForm")
    @JsonIgnore
    private List<RequestFormItem> requestFormItems;

    /**
     * 审核时间
     */
    private Date auditTime;

}
