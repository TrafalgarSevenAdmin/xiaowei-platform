package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
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
 * 报销单
 */
@Table(name = "E_EXPENSEFORM")
@Entity
@Data
@SQLDelete(sql = "update E_EXPENSEFORM set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class ExpenseForm extends BaseEntity {
    /**
     * 所属工单编号
     */
    private String workOrderCode;
    /**
     * 报销单标号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 报销单照片
     */
    private String formFileStore;
    /**
     * 填报总计金额
     */
    private Double fillAmount;
    /**
     * 初审总计金额
     */
    private Double firstTrialAmount;
    /**
     * 复审总计金额
     */
    private Double secondTrialAmount;

    /**
     * 驳回次数
     */
    private Integer turnDownCount;

    /**
     * 初审人
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="e_expenseform_first_trial",
            joinColumns={@JoinColumn(name="EXPENSEFORM_ID")},
            inverseJoinColumns={@JoinColumn(name="FIRSTTRIAL_ID")})
    @JsonIgnore
    private List<SysUser> firstTrials;

    /**
     * 最终初审人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "firstAuditId")
    @Fetch(FetchMode.JOIN)
    private SysUser firstAudit;

    /**
     * 最终复审人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "secondAuditId")
    @Fetch(FetchMode.JOIN)
    private SysUser secondAudit;

    /**
     * 复审人
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="e_expenseform_second_trial",
            joinColumns={@JoinColumn(name="EXPENSEFORM_ID")},
            inverseJoinColumns={@JoinColumn(name="SECONDTRIAL_ID")})
    @JsonIgnore
    private List<SysUser> secondTrials;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 初审审批意见
     */
    private String firstOption;
    /**
     * 复审审批意见
     */
    private String secondOption;

    /**
     * 报销人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "expenseUserId")
    @Fetch(FetchMode.JOIN)
    private SysUser expenseUser;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "expenseForm")
    @JsonIgnore
    private List<ExpenseFormItem> expenseFormItems;

    /**
     * 初审时间
     */
    private Date firstAuditTime;

    /**
     * 复审时间
     */
    private Date secondAuditTime;

    public String getFormFileStore() {
        return UploadConfigUtils.transIdsToPath(this.formFileStore);
    }
}
