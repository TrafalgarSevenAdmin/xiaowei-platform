package com.xiaowei.worksystem.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import com.xiaowei.worksystem.status.ServiceType;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 工单的服务类型
 */
@Entity
@Table(name = "W_WORKORDERTYPE")
@Data
@SQLDelete(sql = "update w_workorder set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class WorkOrderType extends BaseEntity {

    private String name;

    private ServiceType serviceType;
}
