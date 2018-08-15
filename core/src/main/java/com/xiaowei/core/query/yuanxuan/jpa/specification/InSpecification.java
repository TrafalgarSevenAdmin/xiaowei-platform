package com.xiaowei.core.query.yuanxuan.jpa.specification;

import javax.persistence.criteria.*;

public class InSpecification<T> extends AbstractSpecification<T> {
    private String property;
    private Object[] values;

    public InSpecification(String property, Object[] values) {
        this.property = property;
        this.values = values;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return getFieldPath(from,field).in(values);
    }
}
