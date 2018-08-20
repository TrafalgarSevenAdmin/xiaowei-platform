package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Table(name = "E_REQUESTFORMITEM")
@Entity
@Data
public class RequestFormItem extends BaseEntity {

    /**
     * 所属科目编号
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 所属科目内容(json)
     */
    @Lob
    private String subjectContent;
    /**
     * 填报金额
     */
    private Double fillFigure;
    /**
     * 审核金额
     */
    private Double figure;

    /**
     * 明细状态
     */
    private Integer status;
    /**
     * 费用说明
     */
    private String state;

    /**
     * 所属报销单
     */
    @ManyToOne(targetEntity = RequestForm.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "requestFormId")
    @JsonIgnore
    private RequestForm requestForm;

    /**
     * 数量
     */
    private Integer count;

}
