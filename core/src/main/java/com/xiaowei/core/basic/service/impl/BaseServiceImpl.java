package com.xiaowei.core.basic.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.repository.RepositoryWrapper;
import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.query.rundi.query.QuerySpecification;
import com.xiaowei.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mocker
 * @Date 2016-12-10 下午5:03
 * @Version 1.0
 */
public class BaseServiceImpl<T> implements IBaseService<T> {

    private BaseRepository<T> repository;

    private QuerySpecification querySpecification = new QuerySpecification<T>();

    private Class targetClass;

    private RepositoryWrapper<T> repositoryWrapper;

    @Autowired
    private EntityManager entityManager;

    public BaseServiceImpl(BaseRepository<T> repository) {
        this.repository = repository;
        this.repositoryWrapper = new RepositoryWrapper<>(repository);
    }


    @Transactional
    public T save(T t){
        return repository.save(t);
    }

    @Transactional
    public T update(T t) {
        return repository.save(t);
    }

    @Transactional
    public Iterable<T> save(Iterable<T> list) {
        List<T> result = new ArrayList<T>();
        for (T  t : list) {
            result.add(save(t));
        }
        return result;
    }


    @Transactional
    public void delete(Serializable id){
        repository.deleteById(id);
    }

    @Transactional
    public void deleteByQuery(Query query){
        List<T> resoult = this.query(query);
        resoult.stream().forEach(t -> repository.delete(t));
    }

    public T getOne(Serializable id){
        return (T) repository.getOne(id);
    }

    public T findById(Serializable id) {
        if(!StringUtils.isEmpty(id)){
            Optional<T> byId = repository.findById(id);
            if(byId.isPresent()){
                return byId.get();
            }
            return null;
        }else{
            return null;
        }
    }

    public List<T> findByIds(List<Serializable> ids) {
        Query query = new Query();
        query.getFilters().add(Filter.in("id",ids.toArray()));
        return repository.findAll(new QuerySpecification<T>().buildSpecification(query));
    }

    public List<T> findByIds(String[] ids) {
        Query query = new Query();
        query.getFilters().add(Filter.in("id",ids));
        return repository.findAll(new QuerySpecification<T>().buildSpecification(query));
    }


    public List<T> findAll(){
        return  repository.findAll();
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * 高级查询 支持查询条件、分页、排序
     * @param query
     * @return
     */
    public PageResult queryPage(Query query) {
        query.generateCondition();
        Specification specification = querySpecification.buildSpecification(query);
        Page page = null;
        if(specification != null){
            page = repository.findAll(specification, querySpecification.buildPageable(query));
        }else{
            page = repository.findAll(querySpecification.buildPageable(query));
        }
        PageResult pageResult = new PageResult(page.getContent(),page.getTotalPages(),page.getTotalElements(),query.getPage());
        if(query.getResultType() != null && !CollectionUtils.isEmpty(page.getContent())){
            try {
                pageResult.setRows(BeanCopyUtils.copyList(page.getContent(),query.getResultType()));
            } catch (Exception e) {
                throw new RuntimeException("类型转换出错! " + e.getMessage());
            }
        }
        return pageResult;
    }

    @Override
    public PageResult queryPage(Query query, Class targetClass) {
        query.setTargetClass(targetClass);
        return this.queryPage(query);
    }

    /**
     * 高级查询 支持查询条件但不支持分页
     * @param query
     * @return
     */
    public List<T> query(Query query) {
        query.generateCondition();
        Specification specification = querySpecification.buildSpecification(query);
        if(specification != null){
            return repository.findAll(specification,querySpecification.buildSort(query));
        }else{
            return repository.findAll(querySpecification.buildSort(query));
        }
    }

    @Override
    public List<T> query(Query query, Class targetClass) {
        query.setTargetClass(targetClass);
        return this.query(query);
    }


    /**
     * 判断对应Id记录是否存在
     * @param id
     * @return
     */
    public boolean exists(Serializable id) {
        return repository.existsById(id);
    }

    /**
     * 统计总数
     * @return
     */
    public Long count() {
        return repository.count();
    }

    /**
     * 根据条件统计数据
     * @param query
     * @return
     */
    public Long count(Query query) {
        return repository.count(querySpecification.buildSpecification(query));
    }

    public RepositoryWrapper<T> getRepositoryWrapper() {
        return repositoryWrapper;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
