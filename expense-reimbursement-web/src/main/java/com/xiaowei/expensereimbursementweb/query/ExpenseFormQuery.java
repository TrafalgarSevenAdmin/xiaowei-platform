package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import com.xiaowei.expensereimbursement.status.ExpenseFormStatus;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ExpenseFormQuery extends Query {
    private String expenseUserId;
    private String firstTrialId;
    private String secondTrialId;
    private Integer status;
    private String workOrderCode;
    private String firstAuditId;
    private String secondAuditId;
    @DateTimeFormat(pattern = "yyyy-MM-dd Hh:mm:ss")
    private Date b_secondAuditTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd Hh:mm:ss")
    private Date e_secondAuditTime;
    private String departmentId;
    private String userId;
    private String subjectCodeLike;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "createdTime");
        if (StringUtils.isNotEmpty(expenseUserId)) {
            addFilter(new Filter("expenseUser.id", Filter.Operator.eq, expenseUserId));
        }
        if (status != null) {
            addFilter(new Filter("status", Filter.Operator.eq, status));
        }
        if (StringUtils.isNotEmpty(workOrderCode)) {
            addFilter(new Filter("workOrderCode", Filter.Operator.eq, workOrderCode));
        }
        if (StringUtils.isNotEmpty(firstTrialId)) {
            addFilter(new Filter("firstTrials.id", Filter.Operator.eq, firstTrialId));
        }
        if (StringUtils.isNotEmpty(secondTrialId)) {
            addFilter(new Filter("secondTrials.id", Filter.Operator.eq, secondTrialId));
        }
        if (StringUtils.isNotEmpty(firstAuditId)) {
            addFilter(new Filter("firstAudit.id", Filter.Operator.eq, firstAuditId));
        }
        if (StringUtils.isNotEmpty(secondAuditId)) {
            addFilter(new Filter("secondAudit.id", Filter.Operator.eq, secondAuditId));
        }
        //根据复审通过时间过滤
        if (b_secondAuditTime != null && e_secondAuditTime != null) {
            addFilter(new Filter("secondAuditTime", Filter.Operator.between, b_secondAuditTime, e_secondAuditTime));
        }
        //根据员工过滤
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("expenseUser.id", Filter.Operator.eq, userId));
        } else if (StringUtils.isNotEmpty(departmentId)) {
            //根据部门过滤
            addFilter(new Filter("expenseUser.department.id", Filter.Operator.eq, departmentId));
        }
        //根据科目编码过滤
        if (StringUtils.isNotEmpty(subjectCodeLike)) {
            addFilter(new Filter("expenseFormItems.subjectCode", Filter.Operator.like, subjectCodeLike + "%"));
        }

    }

}
