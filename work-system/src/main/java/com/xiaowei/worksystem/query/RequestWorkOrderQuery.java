package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;

@Data
public class RequestWorkOrderQuery extends Query {

    private Integer status;

    @Override
    public void generateCondition() {
        if (status != null) {
            addFilter(new Filter("status", Filter.Operator.eq, status));
        }
    }
}
