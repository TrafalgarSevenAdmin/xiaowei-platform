package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * 报销单
 */
@Table(name = "E_EXPENSEFORM")
@Entity
@Data
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
    @JoinTable(name="e_expenseform_firstTrial",
            joinColumns={@JoinColumn(name="EXPENSEFORM_ID")},
            inverseJoinColumns={@JoinColumn(name="FIRSTTRIAL_ID")})
    @JsonIgnore
    private List<SysUser> firstTrials;

    /**
     * 复审人
     */
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="e_expenseform_secondTrial",
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

    public String getFormFileStore() {
        return UploadConfigUtils.transIdsToPath(this.formFileStore);
    }
}
