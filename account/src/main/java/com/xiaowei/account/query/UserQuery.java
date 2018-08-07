package com.xiaowei.account.query;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.consts.UserStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserQuery extends Query {

    private String loginName;
    private Set<String> roleIds = new HashSet<>();
    private String companyId;
    private String departmentId;
    private String postId;
    private String roleCode;

    @Override
    public void generateCondition() {
        addFilter(new Filter("loginName", Filter.Operator.neq, SuperUser.ADMINISTRATOR_NAME));
        addFilter(new Filter("status", Filter.Operator.neq, UserStatus.DELETE.getStatus()));
        if (StringUtils.isNotEmpty(loginName)) {
            addFilter(new Filter("loginName", Filter.Operator.like, "%" + loginName + "%"));
        }
        if (StringUtils.isNotEmpty(roleCode)) {
            setDistinct(true);
            addFilter(new Filter("roles.code", Filter.Operator.eq, roleCode));
        }
        if (!CollectionUtils.isEmpty(roleIds)) {
            setDistinct(true);
            addFilter(new Filter("roles.id", Filter.Operator.in, roleIds.toArray()));
        }
        if (StringUtils.isNotEmpty(companyId)) {
            setDistinct(true);
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
        if (StringUtils.isNotEmpty(departmentId)) {
            setDistinct(true);
            addFilter(new Filter("department.id", Filter.Operator.eq, departmentId));
        }
        if (StringUtils.isNotEmpty(postId)) {
            setDistinct(true);
            addFilter(new Filter("post.id", Filter.Operator.eq, postId));
        }
    }

}
