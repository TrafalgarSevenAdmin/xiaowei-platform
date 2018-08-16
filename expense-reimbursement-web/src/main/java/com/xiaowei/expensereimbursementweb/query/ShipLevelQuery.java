package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ShipLevelQuery extends Query {
    private String expenseSubjectId;
    private String shipLevel;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(expenseSubjectId)) {
            addFilter(new Filter("expenseSubject.id", Filter.Operator.eq, expenseSubjectId));
        }
        if (StringUtils.isNotEmpty(shipLevel)) {
            addFilter(new Filter("shipLevel", Filter.Operator.eq, shipLevel));
        }
    }

}
