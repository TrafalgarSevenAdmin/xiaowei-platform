package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ShipLevelQuery extends Query {
    private String subjectCode;
    private String shipLevel;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(subjectCode)) {
            addFilter(new Filter("subjectCode", Filter.Operator.eq, subjectCode));
        }
        if (StringUtils.isNotEmpty(shipLevel)) {
            addFilter(new Filter("shipLevel", Filter.Operator.eq, shipLevel));
        }
    }

}
