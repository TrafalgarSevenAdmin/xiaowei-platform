package com.xiaowei.account.query;

import com.xiaowei.account.consts.PostStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PostQuery extends Query {

    private String companyId;

    @Override
    public void generateCondition() {
        addFilter(new Filter("status", Filter.Operator.neq, PostStatus.DELETE.getStatus()));
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
    }
}
