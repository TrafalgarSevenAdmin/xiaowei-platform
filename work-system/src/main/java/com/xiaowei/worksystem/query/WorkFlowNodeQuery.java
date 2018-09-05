package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;

@Data
public class WorkFlowNodeQuery extends Query {
    private Boolean charge;

    @Override
    public void generateCondition() {
        if (charge != null) {
            addFilter(new Filter("charge", Filter.Operator.eq, charge));
        }
    }
}
