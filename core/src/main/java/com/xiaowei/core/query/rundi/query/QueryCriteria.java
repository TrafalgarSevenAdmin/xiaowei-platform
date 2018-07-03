package com.xiaowei.core.query.rundi.query;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * mybatis使用的动态查询构造。
 * @author qinyongliang
 * @Description 动态查询
 * @Version 1.0
 */
public class QueryCriteria {


    /**
     * 生成动态查询
     *
     *
 * @param tableName
 * @param query
     * @return
     */
    public static Example buildExample(String tableName, Query query) {
        return buildExample(tableName, query, null);
    }

    /**
     * 生成动态查询
     *
     * @param query
     * @return
     */
    public static Example buildExample(String tableName, Query query, String returnFields) {

        List<Filter> andFilter = query.getAndFilter();
        List<Filter> orFilter = query.getOrFilter();

        //解析Search
        Search search = query.getSearch();
        if (search != null && !StringUtils.isEmpty(search.getFields()) && !StringUtils.isEmpty(search.getValue())) {
            String[] fields = search.getFields().split(",");
            for (String field : fields) {
                orFilter.add(Filter.like(field, search.getValue()));
            }
        }

        Function<String, String> toField = (field) -> camelToUnderline(field);
        //拼装成example
        Example example = andCriteria(andFilter, toField);
        Example orExample = orCriteria(orFilter, toField);
        example.or(orExample);
        example.setTable(tableName);
        //判断下是不是正确的样式，防止ＳＱＬ注入等等
//        if (query.getFields().matches("^[a-zA-Z0-9,_]*$")) {
//            String[] fields = query.getFields().split(",");//配置需要返回的值
//            //映射一下
//            example.setFields(StringUtils.join(Arrays.stream(fields).map(field -> toField.apply(field) + " as " + field).collect(Collectors.toList()), ","));
//        } else {
//            example.setFields("*");//没有配置就默认选择所有的
//        }
        if(!CollectionUtils.isEmpty(query.getSorts())) {
            List<String> orders = query.getSorts().stream().map(sort -> sort.getField() + " " + sort.getDir().name()).collect(Collectors.toList());
            String order = StringUtils.join(orders, ",");
            if (StringUtils.isNotEmpty(order)) {
                example.setOrderByClause(order);
            }
        }
        example.setNoPage(query.isNoPage());
        example.setPage(query.getPage());
        example.setPageSize(query.getPageSize());
        return example;
    }

    private static Example andCriteria(List<Filter> filters, Function<String,String> fieldCovert) {
        Example example = new Example();
        for (Filter filter : filters) {
            switch (filter.getOperator()) {
                case eq:
                    example.andEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case ge:
                    example.andGreaterThanOrEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case gt:
                    example.andGreaterThan(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;

                case in:
                    example.andIn(fieldCovert.apply(filter.getField()), Arrays.asList((Object[]) filter.getOneValue()));
                    break;
                case isNull:
                    example.andIsNull(fieldCovert.apply(filter.getField()));
                    break;
                case le:
                    example.andLessThanOrEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case like:
                    example.andLike(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case lt:
                    example.andLessThan(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case neq:
                    example.andNotEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case notNull:
                    example.andIsNotNull(fieldCovert.apply(filter.getField()));
                    break;
                case between:
                    example.andBetween(fieldCovert.apply(filter.getField()),filter.getValue()[0],filter.getValue()[1]);
                    break;
            }
        }
        return example;
    }
    private static Example orCriteria(List<Filter> filters,Function<String,String> fieldCovert) {
        Example example = new Example();
        for (Filter filter : filters) {
            switch (filter.getOperator()) {
                case eq:
                    if (filter.getOneValue()!=null) {
                        example.orEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    }
                    break;
                case ge:
                    example.orGreaterThanOrEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case gt:
                    example.orGreaterThan(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case in:
                    example.orIn(fieldCovert.apply(filter.getField()), Arrays.asList((Object[]) filter.getOneValue()));
                    break;
                case isNull:
                    example.orIsNull(fieldCovert.apply(filter.getField()));
                    break;
                case le:
                    example.orLessThanOrEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case like:
                    example.orLike(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case lt:
                    example.orLessThan(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case neq:
                    example.orNotEqualTo(fieldCovert.apply(filter.getField()), filter.getOneValue());
                    break;
                case notNull:
                    example.orIsNotNull(fieldCovert.apply(filter.getField()));
                    break;
                case between:
                    example.andBetween(fieldCovert.apply(filter.getField()),filter.getValue()[0],filter.getValue()[1]);
                    break;
            }
        }
        return example;
    }

    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
