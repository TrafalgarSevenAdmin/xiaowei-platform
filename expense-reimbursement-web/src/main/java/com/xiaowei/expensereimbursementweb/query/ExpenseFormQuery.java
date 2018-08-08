package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;

@Data
public class ExpenseFormQuery extends Query {

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "createdTime");
    }

}
