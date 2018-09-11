package com.xiaowei.account;

import org.hibernate.NullPrecedence;
import org.hibernate.spatial.dialect.postgis.PostgisPG95Dialect;

/**
 * Created by yuanxuan on 2018/1/16.
 * 支持中文排序的方言
 */
public class MyPostgisGBKDialect extends PostgisPG95Dialect {
    public MyPostgisGBKDialect() {

    }

    public String renderOrderByElement(String expression, String collation, String order, NullPrecedence nulls) {
        expression = (new StringBuilder("convert_to(")).append(expression).append(",'gbk')").toString();
        return super.renderOrderByElement(expression, collation, order, nulls);
    }
}
