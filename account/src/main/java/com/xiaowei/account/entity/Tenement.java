package com.xiaowei.account.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Table(name = "SYS_TENEMENT")
@Entity
@SQLDelete(sql = "update SYS_TENEMENT set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class Tenement extends BaseEntity {

    /**
     * 租户编码
     */
    private String code;
    /**
     * 租户名称
     */
    private String name;
    /**
     * 状态
     */
    private Integer status;

}
