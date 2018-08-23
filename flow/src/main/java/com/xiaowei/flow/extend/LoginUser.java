package com.xiaowei.flow.extend;

import java.util.Set;

public interface LoginUser {

    /**
     * 获取登陆用户id
     * @return
     */
    String getUserId();

    /**
     * 获取登陆用户名
     * @return
     */
    String getUserName();

    /**
     * 获取登陆用户角色集合
     * @return
     */
    Set<String> getRoleIds();

    /**
     * 获取登陆用户部门集合
     * @return
     */
    Set<String> getDepartmentIds();

}
