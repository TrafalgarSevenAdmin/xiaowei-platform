package com.xiaowei.worksystem.extend;

import com.google.common.collect.Sets;
import com.xiaowei.account.entity.SysRole;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.context.ContextUtils;
import com.xiaowei.flow.extend.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程扩展登陆用户
 */
@Component
public class SystemLoginUser implements LoginUser {

    @Override
    public String getUserId() {
        return LoginUserUtils.getLoginUser().getId();
    }

    @Override
    public String getUserName() {
        return LoginUserUtils.getLoginUser().getNickName();
    }

    @Override
    public Set<String> getRoleIds() {
        if (LoginUserUtils.getLoginUser().getRoles() != null) {
            return LoginUserUtils.getLoginUser().getRoles().stream().map(v->v.getId()).collect(Collectors.toSet());
        }
        return Sets.newHashSet();
    }

    @Override
    public Set<String> getDepartmentIds() {
        if (LoginUserUtils.getLoginUser().getDepartmentBean() != null) {
            return Sets.newHashSet(LoginUserUtils.getLoginUser().getDepartmentBean().getId());
        }
        return Sets.newHashSet();
    }
}
