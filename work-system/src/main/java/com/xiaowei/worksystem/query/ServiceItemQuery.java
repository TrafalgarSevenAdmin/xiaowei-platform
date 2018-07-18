package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ServiceItemQuery extends Query {
    private String workOrderId;
    @Override
    public void generateCondition() {
        if(StringUtils.isNotEmpty(workOrderId)){
            addFilter(new Filter("workOrder.id", Filter.Operator.eq,workOrderId));
        }
    }
}
