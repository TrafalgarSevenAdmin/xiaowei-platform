package com.xiaowei.core.result;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mocker
 * @Date 2018-03-22 10:13:49
 * @Description 分页信息封装对象
 * @Version 1.0
 */
public class PageResult<T> implements Serializable {

    //数据
    private List<T> rows;

    //总页数
    private Integer totalPage;

    //总数
    private Long totalCount;

    //当前页
    private Integer page;


    public PageResult() {
    }

    public PageResult(List rows,Integer totalPage, Long totalCount,Integer page) {
        this.rows = rows;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.page = page;
    }

    public PageResult(List rows,Integer totalPage, Long totalCount) {
        this.rows = rows;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }


    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }

    public Integer getPage() {
        if(page == null || page.intValue() < 1){
            page = 1;
            return page;
        }
        return page + 1;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
