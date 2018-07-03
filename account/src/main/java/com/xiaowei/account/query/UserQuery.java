package com.xiaowei.account.query;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.consts.UserStatus;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-21 15:11:05
 * @Description 系统权限
 * @Version 1.0
 */
public class UserQuery extends Query {

    private String loginName;
    private Set<String> roleIds = new HashSet<>();
    private Set<String> companyIds = new HashSet<>();
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
        if (!CollectionUtils.isEmpty(companyIds)) {
            setDistinct(true);
            addFilter(new Filter("companies.id", Filter.Operator.in, companyIds.toArray()));
        }
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Set<String> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(Set<String> companyIds) {
        this.companyIds = companyIds;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
