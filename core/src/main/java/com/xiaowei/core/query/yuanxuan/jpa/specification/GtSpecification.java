package com.xiaowei.core.query.yuanxuan.jpa.specification;

import javax.persistence.criteria.*;

public class GtSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Comparable<Object> compare;

    public GtSpecification(String property, Comparable<? extends Object> compare) {
        this.property = property;
        this.compare = (Comparable<Object>) compare;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return cb.greaterThan(getFieldPath(from,field), compare);
    }
}
