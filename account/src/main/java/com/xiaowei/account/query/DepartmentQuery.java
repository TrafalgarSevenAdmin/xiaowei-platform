package com.xiaowei.account.query;

import com.xiaowei.account.consts.DepartmentStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DepartmentQuery extends Query {
    private String userId;
    private String companyId;

    @Override
    public void generateCondition() {
        addFilter(new Filter("status", Filter.Operator.neq, DepartmentStatus.DELETE.getStatus()));
        //根据用户id查询部门
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("users.id", Filter.Operator.eq, userId));
        }
        //根据公司id查询部门
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
    }
}
