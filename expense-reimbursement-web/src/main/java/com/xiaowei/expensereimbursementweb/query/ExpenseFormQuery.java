package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ExpenseFormQuery extends Query {
    private String expenseUserId;
    private Integer status;
    private String workOrderCode;

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

    }

}
