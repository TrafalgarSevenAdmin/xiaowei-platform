package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ReimbursementStandardQuery extends Query {
    private String subjectCode;
    private String shipLevel;
    private String cityLevel;
    private String postLevel;
    private String startCity;
    private String endCity;


    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(subjectCode)) {
            addFilter(new Filter("subjectCode", Filter.Operator.eq, subjectCode));
        }
        if (StringUtils.isNotEmpty(shipLevel)) {
            addFilter(new Filter("shipLevel", Filter.Operator.eq, shipLevel));
        }
        if (StringUtils.isNotEmpty(cityLevel)) {
            addFilter(new Filter("cityLevel", Filter.Operator.eq, cityLevel));
        }
        if (StringUtils.isNotEmpty(postLevel)) {
            addFilter(new Filter("postLevel", Filter.Operator.eq, postLevel));
        }
        if (StringUtils.isNotEmpty(startCity)) {
            addFilter(new Filter("startCity", Filter.Operator.eq, startCity));
        }
        if (StringUtils.isNotEmpty(endCity)) {
            addFilter(new Filter("endCity", Filter.Operator.eq, endCity));
        }
    }
}
