package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ExpenseAccountQuery extends Query {

    private String accountNameLike;
    private String subjectId;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(accountNameLike)) {
            addFilter(new Filter("accountName", Filter.Operator.like, "%" + accountNameLike + "%"));
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            addFilter(new Filter("expenseSubject.id", Filter.Operator.eq, subjectId));
        }
    }
}
