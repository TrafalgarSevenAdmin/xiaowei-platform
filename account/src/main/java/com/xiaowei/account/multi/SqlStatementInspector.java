package com.xiaowei.account.multi;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.Arrays;

public class SqlStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        //无奈之举，如果发现是子查询，就溢出掉filter条件
        if (Arrays.stream(Thread.getAllStackTraces().get(Thread.currentThread())).limit(20).filter(v -> v.getMethodName().equals("loadCollectionSubselect")).findFirst().isPresent()) {
            return sql.replaceAll("\\w*\\.tenancy_id=\\? (and|or)?", "");
        }
        // 这里可以拦截到sql ， 这里的sql格式会有占位符？  如：select u.name from user u where u.id = ?
        return sql;
    }
}
