package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class WorkFlowNodeQuery extends Query {
    private Boolean charge;
    private String code;
    private String serviceTypeLike;
    private Boolean audit;
    private String nodeType;

    @Override
    public void generateCondition() {
        if (charge != null) {
            addFilter(new Filter("charge", Filter.Operator.eq, charge));
        }
        if (StringUtils.isNotEmpty(code)) {
            addFilter(new Filter("code", Filter.Operator.eq, code));
        }
        if (StringUtils.isNotEmpty(serviceTypeLike)) {
            addFilter(new Filter("serviceType", Filter.Operator.like, "%" + serviceTypeLike + "%"));
        }
        if (audit != null) {
            addFilter(new Filter("audit", Filter.Operator.eq, audit));
        }
        if (StringUtils.isNotEmpty(nodeType)) {
            addFilter(new Filter("nodeType", Filter.Operator.eq, nodeType));
        }
    }
}
