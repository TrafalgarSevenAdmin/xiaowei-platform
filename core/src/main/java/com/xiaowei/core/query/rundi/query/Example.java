package com.xiaowei.core.query.rundi.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author administrator
 * @Date 17-9-11
 */
public class Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criterions> oredCriteria = new ArrayList<Criterions>();

    private String fields = "*";
    private String table;

    /**
     * 第多少页
     */
    private Integer page = 1;

    /**
     * 不分页
     */
    private boolean noPage;

    /**
     * 一页显示多少数据
     */
    private Integer pageSize = 10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public boolean isNoPage() {
        return noPage;
    }

    public void setNoPage(boolean noPage) {
        this.noPage = noPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    Example() {
    }

    public Example distinct() {
        distinct = true;
        return this;
    }
    public Example(String table) {
        this.table = table;
    }

    public String getFields() {
        return fields;
    }

    public Example setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criterions> getOredCriteria() {
        return oredCriteria;
    }

    public void setOredCriteria(List<Criterions> oredCriteria) {
        this.oredCriteria = oredCriteria;
    }

    public void or(Example example) {
        oredCriteria.addAll(example.getOredCriteria());
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected Example addCriterion(String condition) {
        if (condition == null) {
            throw new RuntimeException("字段不能为空");
        }
        int size = oredCriteria.size();
        if (size > 0) {
            oredCriteria.get(size - 1).getCriterias().add(new Criterion(condition));
        } else {
            oredCriteria.add(new Criterions(new Criterion(condition)));
        }
        return this;
    }

    protected Example addCriterion(String field,String condition, Object value) {
        if (value == null) {
            throw new RuntimeException(field + "字段的值不能为空");
        }
        int size = oredCriteria.size();
        if (size > 0) {
            oredCriteria.get(size - 1).getCriterias().add(new Criterion(field+condition, value));
        } else {
            oredCriteria.add(new Criterions(new Criterion(field+condition, value)));
        }
        return this;
    }

    protected Example addCriterion(String field,String condition, Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            throw new RuntimeException(field+"区间含有为空的数据");
        }
        int size = oredCriteria.size();
        if (size > 0) {
            oredCriteria.get(size - 1).getCriterias().add(new Criterion(field + condition, value1, value2));
        } else {
            oredCriteria.add(new Criterions(new Criterion(field + condition, value1, value2)));
        }
        return this;
    }

    protected Example orCriterion(String condition) {
        if (condition == null) {
            throw new RuntimeException("字段不能为空");
        }
        oredCriteria.add(new Criterions(new Criterion(condition)));
        return this;
    }

    protected Example orCriterion(String field,String condition, Object value) {
        if (value == null) {
            throw new RuntimeException(field + "字段的值不能为空");
        }
        oredCriteria.add(new Criterions(new Criterion(field+condition, value)));
        return this;
    }

    protected Example orCriterion(String field,String condition, Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            throw new RuntimeException(field+"区间含有为空的数据");
        }
        oredCriteria.add(new Criterions(new Criterion(field + condition, value1, value2)));
        return this;
    }

    public Example andIsNull(String field) {
        addCriterion(field + " is null");
        return (Example) this;
    }

    public Example andIsNotNull(String field) {
        addCriterion(field + " is not null");
        return (Example) this;
    }

    public Example andEqualTo(String field,Object value) {
        addCriterion(field , " =", value);
        return (Example) this;
    }

    public Example andNotEqualTo(String field,Object value) {
        addCriterion(field , " <>", value);
        return (Example) this;
    }

    public Example andGreaterThan(String field,Object value) {
        addCriterion(field , " >", value);
        return (Example) this;
    }

    public Example andGreaterThanOrEqualTo(String field,Object value) {
        addCriterion(field , " >=", value);
        return (Example) this;
    }

    public Example andLessThan(String field,Object value) {
        addCriterion(field , " <", value);
        return (Example) this;
    }

    public Example andLessThanOrEqualTo(String field,Object value) {
        addCriterion(field , " <=", value);
        return (Example) this;
    }

    public Example andLike(String field,Object value) {
        addCriterion(field , " like", value);
        return (Example) this;
    }

    public Example andNotLike(String field,Object value) {
        addCriterion(field , " not like", value);
        return (Example) this;
    }

    public Example andIn(String field,List<Object> values) {
        addCriterion(field , " in", values);
        return (Example) this;
    }

    public Example andNotIn(String field,List<Object> values) {
        addCriterion(field , " not in", values);
        return (Example) this;
    }

    public Example andBetween(String field,Object value1, Object value2) {
        addCriterion(field , " between", value1, value2);
        return (Example) this;
    }

    public Example andNotBetween(String field,Object value1, Object value2) {
        addCriterion(field , " not between", value1, value2);
        return (Example) this;
    }

    public Example orIsNull(String field) {
        orCriterion(field + " is null");
        return (Example) this;
    }

    public Example orIsNotNull(String field) {
        orCriterion(field + " is not null");
        return (Example) this;
    }

    public Example orEqualTo(String field,Object value) {
        orCriterion(field , " =", value);
        return (Example) this;
    }

    public Example orNotEqualTo(String field,Object value) {
        orCriterion(field , " <>", value);
        return (Example) this;
    }

    public Example orGreaterThan(String field,Object value) {
        orCriterion(field , " >", value);
        return (Example) this;
    }

    public Example orGreaterThanOrEqualTo(String field,Object value) {
        orCriterion(field , " >=", value);
        return (Example) this;
    }

    public Example orLessThan(String field,Object value) {
        orCriterion(field , " <", value);
        return (Example) this;
    }

    public Example orLessThanOrEqualTo(String field,Object value) {
        orCriterion(field , " <=", value);
        return (Example) this;
    }

    public Example orLike(String field,Object value) {
        orCriterion(field , " like", value);
        return (Example) this;
    }

    public Example orNotLike(String field,Object value) {
        orCriterion(field , " not like", value);
        return (Example) this;
    }

    public Example orIn(String field,List<Object> values) {
        orCriterion(field , " in", values);
        return (Example) this;
    }

    public Example orNotIn(String field,List<Object> values) {
        orCriterion(field , " not in", values);
        return (Example) this;
    }

    public Example orBetween(String field,Object value1, Object value2) {
        orCriterion(field , " between", value1, value2);
        return (Example) this;
    }

    public Example orNotBetween(String field,Object value1, Object value2) {
        orCriterion(field , " not between", value1, value2);
        return (Example) this;
    }
    public static Example IsNull(String field) {
        return new Example().addCriterion(field + " is null");
    }

    public static Example IsNotNull(String field) {
        return new Example().addCriterion(field + " is not null");
    }

    public static Example EqualTo(String field,Object value) {
        return new Example().addCriterion(field , " =", value);
    }

    public static Example NotEqualTo(String field,Object value) {
        return new Example().addCriterion(field , " <>", value);
    }

    public static Example GreaterThan(String field,Object value) {
        return new Example().addCriterion(field , " >", value);
    }

    public static Example GreaterThanOrEqualTo(String field,Object value) {
        return new Example().addCriterion(field , " >=", value);
    }

    public static Example LessThan(String field,Object value) {
        return new Example().addCriterion(field , " <", value);
    }

    public static Example LessThanOrEqualTo(String field,Object value) {
        return new Example().addCriterion(field , " <=", value);
    }

    public static Example Like(String field,Object value) {
        return new Example().addCriterion(field , " like", value);
    }

    public static Example NotLike(String field,Object value) {
        return new Example().addCriterion(field , " not like", value);
    }

    public static Example In(String field,List<Object> values) {
        return new Example().addCriterion(field , " in", values);
    }

    public static Example NotIn(String field,List<Object> values) {
        return new Example().addCriterion(field , " not in", values);
    }

    public static Example Between(String field,Object value1, Object value2) {
        return new Example().addCriterion(field , " between", value1, value2);
    }

    public static Example NotBetween(String field,Object value1, Object value2) {
        return new Example().addCriterion(field , " not between", value1, value2);
    }


    class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }


        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
    class Criterions{
        List<Criterion> criterias = new ArrayList<>();


        public Criterions(List<Criterion> criterias) {
            this.criterias = criterias;
        }
        public Criterions(Criterion criteria) {
            this.criterias.add(criteria);
        }

        public List<Criterion> getCriterias() {
            return criterias;
        }

        public void setCriterias(List<Criterion> criterias) {
            this.criterias = criterias;
        }
    }
}
