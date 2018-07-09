package com.xiaowei.core.query.rundi.query;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouyang
 * @Date 2017-04-15 上午9:44
 * @Description 查询对象
 * @Version 1.0
 */

@Data
public class Query implements Serializable{
    /**
     * 目标实体class
     */
    private Class targetClass;

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

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

    /**
     * 多字段模糊查询
     */
    private Search search;

    /**
     * 拼装And条件
     */
    private List<Filter> filters = new ArrayList<Filter>();

    /**
     * 拼装查询条件
     */
    private List<Sort> sorts;

    private Class resultType;
    private Boolean distinct = false;

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * 连接条件
     */
    private Logic logic = Logic.and;

    public Query() {
    }

    public Query(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public void addSort(Sort.Dir dir, String field){
        if(sorts == null){
            sorts = new ArrayList<>();
        }
        Sort sort = new Sort();
        if (dir == null) {
            sort.setDir(Sort.Dir.desc);
        } else {
            sort.setDir(dir);
        }
        sort.setField(field);
        sorts.add(sort);
    }

    /**
     * 生成条件
     */
    public void generateCondition(){

    }

    public Query addFilter(Filter filter){
        filters.add(filter);
        return this;
    }

    public Integer getPage() {
        if(page <= 1){
            return 0;
        }
        return page-1;
    }

    public Query setPage(Integer page) {
        this.page = page;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Query setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Query setFilters(List<Filter> filters) {
        this.filters = filters;
        return this;
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public Query setSorts(List<Sort> sorts) {
        this.sorts = sorts;
        return this;
    }

    public Logic getLogic() {
        return logic;
    }

    public Query setLogic(Logic logic) {
        this.logic = logic;
        return this;
    }

    protected List<Filter> getOrFilter(){
        List<Filter> orFilters = new ArrayList<>();
        for (Filter filter : filters) {
            if(filter.getLogic() != Logic.and){
                if(filter.getLogic() != null && filter.getLogic() == Logic.or){
                    orFilters.add(filter);
                }else if(logic.equals(Logic.or)){
                    orFilters.add(filter);
                }
            }
        }
        return orFilters;
    }

    protected List<Filter> getAndFilter(){
        List<Filter> andFilters = new ArrayList<>();
        for (Filter filter : filters) {
            if(filter.getLogic() != Logic.or){
                if(filter.getLogic() == Logic.and){
                    andFilters.add(filter);
                }else if(logic.equals(Logic.and)){
                    andFilters.add(filter);
                }
            }
        }
        return andFilters;
    }

    public Class getResultType() {
        return resultType;
    }

    public Query setResultType(Class resultType) {
        this.resultType = resultType;
        return this;
    }

    public Search getSearch() {
        return search;
    }

    public Query setSearch(Search search) {
        this.search = search;
        return this;
    }

    public boolean isNoPage() {
        return noPage;
    }

    public Query setNoPage(boolean noPage) {
        this.noPage = noPage;
        return this;
    }

    public int getBeginIndex() {
        return (getPage() - 1) * pageSize;
    }

    public static Sort.Dir getSortType(String sort){
        if("desc".equals(sort)){
            return Sort.Dir.desc;
        }else if("asc".equals(sort)){
            return Sort.Dir.asc;
        }
        //默认倒叙排序
        return Sort.Dir.desc;
    }
}
