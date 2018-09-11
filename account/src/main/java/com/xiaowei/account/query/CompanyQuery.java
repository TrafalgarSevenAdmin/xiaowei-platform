package com.xiaowei.account.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mocker
 * @Date 2018-03-21 15:11:05
 * @Description 系统权限
 * @Version 1.0
 */
@Data
public class CompanyQuery extends Query {
    private String companyId;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.asc, "companyName");
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("id", Filter.Operator.eq, companyId));
        }
    }

}
