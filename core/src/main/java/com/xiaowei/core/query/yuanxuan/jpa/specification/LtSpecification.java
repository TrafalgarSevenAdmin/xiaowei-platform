package com.xiaowei.core.query.yuanxuan.jpa.specification;

import javax.persistence.criteria.*;

public class LtSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Comparable<Object> compare;

    public LtSpecification(String property, Comparable<? extends Object> compare) {
        this.property = property;
        this.compare = (Comparable<Object>) compare;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        return cb.lessThan(getFieldPath(from,field), compare);
    }
}
