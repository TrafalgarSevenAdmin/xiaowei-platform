package com.xiaowei.account.multi;

import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.core.context.ContextUtils;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import javax.persistence.EntityManager;
import java.util.regex.Pattern;

import static com.xiaowei.account.multi.consts.MultiConsts.MultiTenancyFilterName;

public class SqlStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        Pattern compile = Pattern.compile("tenancy_id=\\?");
        // 这里可以拦截到sql ， 这里的sql格式会有占位符？  如：select u.name from user u where u.id = ?
        Filter enabledFilter;
        try {
            enabledFilter = ContextUtils.getApplicationContext().getBean(EntityManager.class).unwrap(Session.class).getEnabledFilter(MultiTenancyFilterName);
        } catch (Exception e) {
            enabledFilter = null;
        }
        if (enabledFilter == null && sql.contains("tenancy_id=?")) {
            //替换所有租户过滤配置
            return compile.matcher(sql).replaceAll("tenancy_id=\'" + LoginUserUtils.getLoginUser().getTenancyId() + "\'");
        }
        return sql;
    }
}
