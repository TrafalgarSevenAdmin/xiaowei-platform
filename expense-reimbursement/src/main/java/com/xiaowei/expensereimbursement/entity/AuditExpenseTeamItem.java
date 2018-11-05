package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.entity.SysUser;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Table(name = "E_AUDITEXPENSETEAMITEM")
@Entity
@Data
public class AuditExpenseTeamItem extends MultiBaseEntity {
    /**
     * 审核人
     */
    @ManyToOne(targetEntity = SysUser.class)
    @JoinColumn(name = "auditId")
    @Fetch(FetchMode.JOIN)
    private SysUser audit;
    /**
     * 审核顺序
     */
    private Integer orderNumber;
    /**
     * 所属审核小组
     */
    @ManyToOne(targetEntity = AuditExpenseTeam.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    @JsonIgnore
    private AuditExpenseTeam auditExpenseTeam;


}
