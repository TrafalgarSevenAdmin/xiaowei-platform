package com.xiaowei.core.query.yuanxuan.jpa.specification;

import javax.persistence.criteria.*;

public class NotLikeSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final String[] patterns;

    public NotLikeSpecification(String property, String... patterns) {
        this.property = property;
        this.patterns = patterns;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        From from = getRoot(property, root);
        String field = getProperty(property);
        if (patterns.length == 1) {
            return cb.like(getFieldPath(from,field), patterns[0]).not();
        }
        Predicate[] predicates = new Predicate[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            predicates[i] = cb.like(getFieldPath(from,field), patterns[i]).not();
        }
        return cb.or(predicates);
    }
}
