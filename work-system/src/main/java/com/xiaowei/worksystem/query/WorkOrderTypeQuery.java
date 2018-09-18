package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.worksystem.status.ServiceType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class WorkOrderTypeQuery extends Query {
    private String name;
    private ServiceType serviceType;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(name)) {
            addFilter(new Filter("name", Filter.Operator.eq, name));
        }
        if (serviceType != null) {
            addFilter(new Filter("serviceType", Filter.Operator.eq, serviceType));
        }
    }
}
