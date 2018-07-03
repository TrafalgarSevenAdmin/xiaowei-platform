package com.xiaowei.account.query;

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
public class PermissionQuery extends Query {
    private String name;
    private Set<String> roleIds = new HashSet<>();

    @Override
    public void generateCondition() {
//        addFilter(new Filter("name", Filter.Operator.neq, "权限系统"));
        if (StringUtils.isNotEmpty(name)) {
            addFilter(new Filter("name", Filter.Operator.like, "%" + name + "%"));
        }
        if (!CollectionUtils.isEmpty(roleIds)) {
            setDistinct(true);
            addFilter(new Filter("roles.id", Filter.Operator.in, roleIds.toArray()));
        }
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
