package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ExpenseSubjectQuery extends Query {

    private String subjectNameLike;
    private String subjectCode;
    private Sort.Dir levelSort;

    @Override
    public void generateCondition() {
        if (Sort.Dir.asc.equals(levelSort)) {
            addSort(Sort.Dir.asc, "level");
        } else if (Sort.Dir.desc.equals(levelSort)) {
            addSort(Sort.Dir.desc, "level");
        }
        if (StringUtils.isNotEmpty(subjectNameLike)) {
            addFilter(new Filter("subjectName", Filter.Operator.like, "%" + subjectNameLike + "%"));
        }
        if (StringUtils.isNotEmpty(subjectCode)) {
            addFilter(new Filter("code", Filter.Operator.eq, subjectCode));
        }
    }
}
