package com.xiaowei.accountweb;

import com.xiaowei.accountcommon.LoginUserUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.Arrays;

public class SqlStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {

        //无奈之举，如果发现是子查询，就溢出掉filter条件
        if (Arrays.stream(Thread.getAllStackTraces().get(Thread.currentThread())).limit(20).filter(v -> v.getMethodName().equals("loadCollectionSubselect")).findFirst().isPresent()) {
            return sql.replace("tenancy_id=?", "tenancy_id=\'" + LoginUserUtils.getLoginUser().getTenancyId() + "\'");
        }
        // 这里可以拦截到sql ， 这里的sql格式会有占位符？  如：select u.name from user u where u.id = ?
        return sql;
    }
}
