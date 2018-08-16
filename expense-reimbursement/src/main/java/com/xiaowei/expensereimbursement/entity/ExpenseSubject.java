package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 费用科目实体
 */
@Table(name = "E_EXPENSESUBJECT")
@Entity
@Data
@SQLDelete(sql = "update E_EXPENSESUBJECT set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
public class ExpenseSubject extends BaseEntity {
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 父级科目id
     */
    private String parentId;
    /**
     * 科目代码
     */
    @Column(updatable = false)
    private Integer ownCode;
    /**
     * 科目编号
     */
    @Column(unique = true,updatable = false)
    private String code;
    /**
     * 科目等级
     */
    private Integer level;

    /**
     * 科目内容
     */
    @Lob
    private String accountContent;

    /**
     * 科目说明
     */
    private String state;

    /**
     * 税率
     */
    private Float taxRate;

    /**
     * 计费类型
     */
    private Integer costType;
    /**
     * 是否按照城市
     */
    private Boolean city;
    /**
     * 是否按照城市级别
     */
    private Boolean cityLevel;
    /**
     * 是否按照舱位级别
     */
    private Boolean shipLevel;
    /**
     * 是否按照岗位级别
     */
    private Boolean postLevel;

}
