package com.xiaowei.account.query;

import com.xiaowei.account.consts.CompanyStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mocker
 * @Date 2018-03-21 15:11:05
 * @Description 系统权限
 * @Version 1.0
 */
public class CompanyQuery extends Query {
    private String userId;

    @Override
    public void generateCondition() {
        addFilter(new Filter("status", Filter.Operator.neq, CompanyStatus.DELETE.getStatus()));
        //根据用户id查询公司
        if (StringUtils.isNotEmpty(userId)) {
            addFilter(new Filter("users.id", Filter.Operator.eq, userId));
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
