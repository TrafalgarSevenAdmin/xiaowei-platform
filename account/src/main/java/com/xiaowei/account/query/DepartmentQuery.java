package com.xiaowei.account.query;

import com.xiaowei.account.consts.DepartmentStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class DepartmentQuery extends Query {
    private String companyId;

    @Override
    public void generateCondition() {
        //根据公司id查询部门
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
    }
}
