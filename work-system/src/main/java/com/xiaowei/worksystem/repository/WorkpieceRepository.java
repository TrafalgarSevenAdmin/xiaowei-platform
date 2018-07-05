package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.Workpiece;
import org.springframework.data.jpa.repository.Query;

public interface WorkpieceRepository extends BaseRepository<Workpiece> {

    @Query("select w from Workpiece w where code = ?1 and storeType = ?2 and status <> 99 ")
    Workpiece findByCodeAndStoreType(String code, Integer storeType);

    /**
     * 查询此工程师下的某个工件
     * @param code
     * @param userId
     * @return
     */
    @Query("select w from Workpiece w where code = ?1 and userId = ?2 and status <> 99 ")
    Workpiece findByCodeAndUserId(String code,String userId);
}
