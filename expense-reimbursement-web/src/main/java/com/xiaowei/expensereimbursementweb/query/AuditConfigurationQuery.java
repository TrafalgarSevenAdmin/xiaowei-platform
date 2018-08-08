package com.xiaowei.expensereimbursementweb.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class AuditConfigurationQuery extends Query {
    private String userId;
    private String departmentId;
    private Integer typeStatus;

    @Override
    public void generateCondition() {
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("userId", Filter.Operator.eq, userId));
        }
        if (StringUtils.isNotEmpty(departmentId)) {
            addFilter(new Filter("departmentId", Filter.Operator.eq, departmentId));
        }
        if (typeStatus != null) {
            addFilter(new Filter("typeStatus", Filter.Operator.eq, typeStatus));
        }
    }

}
