package com.xiaowei.expensereimbursement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.utils.UploadConfigUtils;
import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 报销单明细
 */
@Table(name = "E_EXPENSEFORMITEM")
@Entity
@Data
public class ExpenseFormItem extends BaseEntity {
    /**
     * 所属科目编号
     */
    private String subjectCode;
    /**
     * 所属科目内容(json)
     */
    private String subjectContent;
    /**
     * 填报金额
     */
    private Double fillFigure;
    /**
     * 初审金额
     */
    private Double firstFigure;
    /**
     * 复审金额
     */
    private Double secondFigure;
    /**
     * 明细状态
     */
    private Integer status;
    /**
     * 费用说明
     */
    private String state;
    /**
     * 发票照片
     */
    private String invoiceFileStore;
    /**
     * 支付凭证
     */
    private String voucherFileStore;
    /**
     * 所属报销单
     */
    @ManyToOne(targetEntity = ExpenseForm.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseFormId")
    @JsonIgnore
    private ExpenseForm expenseForm;

    public List<FileStore> getInvoiceFileStore() {
        return UploadConfigUtils.transIdsToPath(this.invoiceFileStore);
    }

    public List<FileStore> getVoucherFileStore() {
        return UploadConfigUtils.transIdsToPath(this.voucherFileStore);
    }

}
