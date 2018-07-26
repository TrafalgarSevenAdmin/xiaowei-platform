package com.xiaowei.worksystem.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ServiceItemQuery extends Query {

    private String workOrderId;
    private Sort.Dir orderNumber;
    private Integer status;

    @Override
    public void generateCondition() {
        //根据排序号进行排序
        if (Sort.Dir.desc.equals(orderNumber)) {
            addSort(Sort.Dir.desc, "orderNumber");
        } else if (Sort.Dir.asc.equals(orderNumber)) {
            addSort(Sort.Dir.asc, "orderNumber");
        }
        if (StringUtils.isNotEmpty(workOrderId)) {
            addFilter(new Filter("workOrder.id", Filter.Operator.eq, workOrderId));
        }
        if (status != null) {
            addFilter(new Filter("status", Filter.Operator.eq, status));
        }
    }
}
