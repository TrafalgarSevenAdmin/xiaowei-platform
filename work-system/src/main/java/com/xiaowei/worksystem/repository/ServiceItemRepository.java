package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.ServiceItem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceItemRepository extends BaseRepository<ServiceItem>{
    @Query("select s from ServiceItem s where s.workOrder.id = ?1")
    List<ServiceItem> findByWorkOrderId(String workOrderId);

    @Query("select s from ServiceItem s where s.workOrder.id = ?1 and s.status = ?2")
    List<ServiceItem> findByWorkOrderIdAndStatus(String workOrderId, Integer status);
}
