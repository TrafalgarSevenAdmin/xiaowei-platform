package com.xiaowei.account.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class AuditConfigurationQuery extends Query {
    private String userId;
    private String departmentId;
    private Integer typeStatus;
    private List<String> departmentIds;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.asc, "typeStatus");
        addSort(Sort.Dir.asc, "departmentId");
        addSort(Sort.Dir.asc, "userId");
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("userId", Filter.Operator.eq, userId));
        }
        if (StringUtils.isNotEmpty(departmentId)) {
            addFilter(new Filter("departmentId", Filter.Operator.eq, departmentId));
        }
        if (typeStatus != null) {
            addFilter(new Filter("typeStatus", Filter.Operator.eq, typeStatus));
        }
        if (CollectionUtils.isNotEmpty(departmentIds)) {
            addFilter(new Filter("departmentId", Filter.Operator.in, Logic.or, departmentIds));
        }
    }
}
