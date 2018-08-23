package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class RequestFormQuery extends Query {
    private String workOrderCode;
    private String code;
    private String trialId;
    private String auditId;
    private Integer status;
    private String requestUserId;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "createdTime");
        if (StringUtils.isNotEmpty(workOrderCode)) {
            addFilter(new Filter("workOrderCode", Filter.Operator.eq, workOrderCode));
        }
        if (StringUtils.isNotEmpty(code)) {
            addFilter(new Filter("code", Filter.Operator.eq, code));
        }
        if (StringUtils.isNotEmpty(trialId)) {
            addFilter(new Filter("trials.id", Filter.Operator.eq, trialId));
        }
        if (StringUtils.isNotEmpty(auditId)) {
            addFilter(new Filter("audit.id", Filter.Operator.eq, auditId));
        }
        if (status!=null) {
            addFilter(new Filter("status", Filter.Operator.eq, status));
        }
        if (StringUtils.isNotEmpty(requestUserId)) {
            addFilter(new Filter("requestUser.id", Filter.Operator.eq, requestUserId));
        }
    }
}
