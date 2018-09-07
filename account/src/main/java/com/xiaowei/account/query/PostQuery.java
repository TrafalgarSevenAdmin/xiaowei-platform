package com.xiaowei.account.query;

import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.Sort;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PostQuery extends Query {

    private String companyId;
    private String postName;

    @Override
    public void generateCondition() {
        addSort(Sort.Dir.desc, "postName");
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
        if (StringUtils.isNotEmpty(postName)) {
            addFilter(new Filter("postName", Filter.Operator.like, "%" + postName + "%"));
        }
    }
}
