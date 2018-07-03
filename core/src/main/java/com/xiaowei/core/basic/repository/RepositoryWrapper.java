package com.xiaowei.core.basic.repository;

import com.xiaowei.core.context.ContextUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhouyang
 * @Date 2017-05-11 11:42
 * @Description dao包装 增强dao功能
 * @Version 1.0
 */
public class RepositoryWrapper<T> implements BaseRepository<T> {

    private BaseRepository<T> baseRepository;

    private EntityManager entityManager;

    public RepositoryWrapper(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public EntityManager getEntityManager() {
        if (this.entityManager == null) {
            this.entityManager = ContextUtils.getApplicationContext().getBean(EntityManager.class);
        }
        return entityManager;
    }

    /**
     * 创建本地SQL查询 返回List<Map>结构
     *
     * @param sql
     * @return
     */
    public List<Map> createNativeQueryResultMap(String sql) {
        HibernateEntityManager hibernateImpl = (HibernateEntityManager) getEntityManager();
        Session session = hibernateImpl.getSession();
        List result = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        return result;
    }

    /**
     * 创建本地查询返回单个Map
     *
     * @param sql
     * @return
     */
    public Map createNativeQueryResultSingleMap(String sql) {
        HibernateEntityManager hibernateImpl = (HibernateEntityManager) getEntityManager();
        Session session = hibernateImpl.getSession();
        List<Map> result = session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        if (!CollectionUtils.isEmpty(result)) {
            return result.get(0);
        }
        return null;
    }


    /**
     * 创建本地查询返回实体
     *
     * @param sql
     * @return
     */
    public <F> F createNativeQueryResultSingleEntity(String sql, Class<F> entityClass) {
        List<F> entitys = createNativeQueryResultEntity(sql, entityClass);
        return entitys.size() > 0 ? entitys.get(0) : null;
    }

    /**
     * 创建本地查询返回实体 单条数据
     *
     * @param sql
     * @return
     */
    public <F> List<F> createNativeQueryResultEntity(String sql, Class<F> entityClass) {
        List<Map> resultMap = createNativeQueryResultMap(sql);
        List<F> entitys = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultMap)) {
            for (Map map : resultMap) {
                try {
                    F f = entityClass.newInstance();
                    BeanUtils.populate(f, map);
                    entitys.add(f);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return entitys;
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    public List<T> findAllById(Iterable<Serializable> iterable) {
        return baseRepository.findAllById(iterable);
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public void deleteById(Serializable serializable) {
        baseRepository.deleteById(serializable);
    }

    @Override
    public void delete(T t) {
        baseRepository.delete(t);
    }

    @Override
    public void deleteAll(Iterable<? extends T> iterable) {
        baseRepository.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        baseRepository.deleteAll();
    }

    @Override
    public <S extends T> S save(S s) {
        return baseRepository.save(s);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> iterable) {
        return baseRepository.saveAll(iterable);
    }

    @Override
    public Optional<T> findById(Serializable serializable) {
        return baseRepository.findById(serializable);
    }

    @Override
    public boolean existsById(Serializable serializable) {
        return baseRepository.existsById(serializable);
    }

    @Override
    public void flush() {
        baseRepository.flush();
    }

    @Override
    public <S extends T> S saveAndFlush(S s) {
        return baseRepository.saveAndFlush(s);
    }

    @Override
    public void deleteInBatch(Iterable<T> iterable) {
        baseRepository.deleteInBatch(iterable);
    }

    @Override
    public void deleteAllInBatch() {
        baseRepository.deleteAllInBatch();
    }

    @Override
    public T getOne(Serializable serializable) {
        return baseRepository.getOne(serializable);
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return baseRepository.findOne(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return baseRepository.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return baseRepository.findAll(example,sort);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return baseRepository.findAll(example,pageable);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return baseRepository.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return baseRepository.exists(example);
    }

    @Override
    public Optional<T> findOne(Specification<T> specification) {
        return baseRepository.findOne(specification);
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return baseRepository.findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return baseRepository.findAll(specification,pageable);
    }

    @Override
    public List<T> findAll(Specification<T> specification, Sort sort) {
        return baseRepository.findAll(specification,sort);
    }

    @Override
    public long count(Specification<T> specification) {
        return baseRepository.count(specification);
    }
}
