package com.xiaowei.core.basic.service;


import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mocker
 * @Date 2016-12-10 下午5:28
 * @Version 1.0
 * @Description 对基本业务方法进行封装
 */
public interface IBaseService<T> {

    /**
     * 添加或更新
     * @param t
     */
    public T save(T t);

    /**
     * 更新
     * @param t
     * @return
     */
    public T update(T t);

    /**
     * 添加或更新
     * @param t
     */
    public Iterable<T> save(Iterable<T> t);

    /**
     * 根据主键删除
     * @param id
     */
    public void delete(Serializable id);

    /**
     * 根据查询条件删除
     * @param query
     */
    public void deleteByQuery(Query query);

    /**
     * 根据主键获取 懒加载
     * @param id
     * @return
     */
    public T getOne(Serializable id);

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    public T findById(Serializable id);

    /**
     * 根据主键获取
     * @param ids
     * @return
     */
    public List<T> findByIds(List<Serializable> ids);

    /**
     * 根据主键获取
     * @param ids
     * @return
     */
    public List<T> findByIds(String[] ids);



    /**
     * 获取全部记录
     * @return
     */
    public List<T> findAll();


    /**
     * 删除全部
     */
    void deleteAll();

    /**
     * 高级查询 支持查询条件、分页、排序
     * @param query 查询条件
     * @return
     */
    PageResult queryPage(Query query);
    PageResult queryPage(Query query, Class targetClass);

    /**
     * 高级查询 支持查询条件但不支持分页
     * @param query  查询条件
     * @return
     */
    List<T> query(Query query);
    List<T> query(Query query, Class targetClass);


    /**
     * 判断对应Id记录是否存在
     * @param id 数据ID
     * @return
     */
    public boolean exists(Serializable id);

    /**
     * 统计总数
     * @return
     */
    public Long count();

    /**
     * 根据条件统计数据
     * @param query
     * @return
     */
    public Long count(Query query);


}
