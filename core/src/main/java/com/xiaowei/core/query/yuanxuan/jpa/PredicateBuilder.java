package com.xiaowei.core.query.yuanxuan.jpa;

import com.xiaowei.core.query.yuanxuan.jpa.specification.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.criteria.Predicate.BooleanOperator.OR;

public class PredicateBuilder<T> {

    private final Predicate.BooleanOperator operator;

    private List<Specification<T>> specifications;
    private Class<T> targetClass;
    private Boolean distinct = false;

    public Class<T> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public PredicateBuilder(Predicate.BooleanOperator operator) {
        this.operator = operator;
        this.specifications = new ArrayList<>();
    }

    public PredicateBuilder<T> eq(String property, Object... values) {
        return eq(true, property, values);
    }

    public PredicateBuilder<T> eq(boolean condition, String property, Object... values) {
        return this.predicate(condition, new EqualSpecification<T>(property, values));
    }

    public PredicateBuilder<T> ne(String property, Object... values) {
        return ne(true, property, values);
    }

    public PredicateBuilder<T> ne(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotEqualSpecification<T>(property, values));
    }

    public PredicateBuilder<T> gt(String property, Comparable<?> compare) {
        return gt(true, property, compare);
    }

    public PredicateBuilder<T> gt(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new GtSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> ge(String property, Comparable<?> compare) {
        return ge(true, property, compare);
    }

    public PredicateBuilder<T> ge(boolean condition, String property, Comparable<? extends Object> compare) {
        return this.predicate(condition, new GeSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> lt(String property, Comparable<?> number) {
        return lt(true, property, number);
    }

    public PredicateBuilder<T> lt(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LtSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> le(String property, Comparable<?> compare) {
        return le(true, property, compare);
    }

    public PredicateBuilder<T> le(boolean condition, String property, Comparable<?> compare) {
        return this.predicate(condition, new LeSpecification<T>(property, compare));
    }

    public PredicateBuilder<T> between(String property, Range<? extends Comparable<?>> range) {
        return between(true, property, range);
    }

    public PredicateBuilder<T> between(boolean condition, String property, Range<? extends Comparable<?>> range) {
        return this.predicate(condition, new BetweenSpecification<T>(property, range));
    }

    public PredicateBuilder<T> like(String property, String... patterns) {
        return like(true, property, patterns);
    }

    public PredicateBuilder<T> like(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new LikeSpecification<T>(property, patterns));
    }

    public PredicateBuilder<T> notLike(String property, String... patterns) {
        return notLike(true, property, patterns);
    }

    public PredicateBuilder<T> notLike(boolean condition, String property, String... patterns) {
        return this.predicate(condition, new NotLikeSpecification<T>(property, patterns));
    }

    public PredicateBuilder<T> in(String property, Object... values) {
        return this.in(true, property, values);
    }

    public PredicateBuilder<T> in(boolean condition, String property, Object... values) {
        return this.predicate(condition, new InSpecification<T>(property, values));
    }

    public PredicateBuilder<T> notIn(String property, Object... values) {
        return this.notIn(true, property, values);
    }

    public PredicateBuilder<T> notIn(boolean condition, String property, Object... values) {
        return this.predicate(condition, new NotInSpecification<T>(property, values));
    }

    public PredicateBuilder<T> predicate(Specification specification) {
        return predicate(true, specification);
    }

    public PredicateBuilder<T> predicate(boolean condition, Specification specification) {
        if (condition) {
            this.specifications.add(specification);
        }
        return this;
    }

    public Specification<T> build() {
        return (root, query, cb) -> {
            Predicate[] predicates = new Predicate[specifications.size()];
            query.distinct(distinct);
            setFetch(root);
            for (int i = 0; i < specifications.size(); i++) {
                predicates[i] = specifications.get(i).toPredicate(root, query, cb);
            }
            return OR.equals(operator) ? cb.or(predicates) : cb.and(predicates);
        };
    }

    /**
     * 设置root的fetch条件,用于查询时立即加载从表对象
     * @param root
     */
    private void setFetch(Root<T> root) {
        Class<T> targetClass = this.getTargetClass();
        if(targetClass==null){
            return;
        }
        //获取所有字段
        Field[] fields = targetClass.getDeclaredFields();
        //遍历字段
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            Fetch fetch = field.getAnnotation(Fetch.class);
            //如果fetch时join,那么root设置此字段得到fetch以便于立即加载,否则就不管
            if(fetch!=null&& FetchMode.JOIN.equals(fetch.value())){
                root.fetch(field.getName(), JoinType.LEFT);
            }
            field.setAccessible(false);
        });
    }

}
