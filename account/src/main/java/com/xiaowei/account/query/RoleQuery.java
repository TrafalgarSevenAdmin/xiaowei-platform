package com.xiaowei.account.query;

import com.xiaowei.account.consts.RoleType;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Logic;
import com.xiaowei.core.query.rundi.query.Query;
import lombok.Data;
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
@Data
public class RoleQuery extends Query {

    private String name;
    private Set<String> userIds = new HashSet<>();
    private String companyId;
    private String companyId2;
    private Integer roleType;

    @Override
    public void generateCondition() {

        if (StringUtils.isNotEmpty(name)) {
            addFilter(new Filter("name", Filter.Operator.like, "%" + name + "%"));
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            setDistinct(true);
            addFilter(new Filter("users.id", Filter.Operator.in, userIds.toArray()));
        }
        if (StringUtils.isNotEmpty(companyId)) {
            addFilter(new Filter("company.id", Filter.Operator.eq, companyId));
        }
        if (roleType != null) {
            addFilter(new Filter("roleType", Filter.Operator.eq, roleType));
        }
        if (StringUtils.isNotEmpty(companyId2)) {
            addFilter(new Filter("company.id", Filter.Operator.eq,Logic.or, companyId2));
            addFilter(new Filter("roleType", Filter.Operator.eq, Logic.or, RoleType.TRUSTEESHIPROLE.getStatus()));
        }
    }

}
