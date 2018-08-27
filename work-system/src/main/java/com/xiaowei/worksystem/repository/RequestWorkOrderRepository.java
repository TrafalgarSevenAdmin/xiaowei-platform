package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.RequestWorkOrder;
import org.springframework.data.jpa.repository.Query;

public interface RequestWorkOrderRepository extends BaseRepository<RequestWorkOrder>{
    @Query("select count(r) from RequestWorkOrder r where r.status = ?1")
    Long findCountByStatus(Integer status);
}
