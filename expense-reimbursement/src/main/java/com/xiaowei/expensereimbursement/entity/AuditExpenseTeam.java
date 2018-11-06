package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import com.xiaowei.expensereimbursement.status.AuditExpenseType;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Table(name = "E_AUDITEXPENSETEAM")
@Entity
@Data
@SQLDelete(sql = "update E_AUDITEXPENSETEAM set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class AuditExpenseTeam extends MultiBaseEntity {
    /**
     * 小组名称
     */
    private String teamName;
    /**
     * 小组编号
     */
    @Column(updatable = false)
    private String code;
    /**
     * 小组审核类型
     */
    private AuditExpenseType auditExpenseType;
    /**
     * 小组明细
     */
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "auditExpenseTeam")
    @JsonIgnore
    private List<AuditExpenseTeamItem> auditExpenseTeamItems;
}
