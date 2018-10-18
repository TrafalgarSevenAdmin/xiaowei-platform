package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Entity;

@Data
@Entity
@Subselect("select * from w_workorder")
@Synchronize({"w_workorder"})
public class WorkOrderSelect extends MultiBaseEntity {
    private String code;
    private Integer systemStatus;
}
