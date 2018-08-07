package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ExpenseSubjectQuery extends Query {

    private String subjectNameLike;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(subjectNameLike)) {
            addFilter(new Filter("subjectName", Filter.Operator.like, "%" + subjectNameLike + "%"));
        }
    }
}