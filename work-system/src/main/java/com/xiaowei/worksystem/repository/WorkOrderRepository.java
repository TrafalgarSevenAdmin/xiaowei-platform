package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.WorkOrder;
import org.springframework.data.jpa.repository.Query;

public interface WorkOrderRepository extends BaseRepository<WorkOrder>{

    @Query("select count(w) from WorkOrder w where w.systemStatus in ?2 and w.engineer.id = ?1")
    Long findByEngineerIdAndStatusIn(String engineerId, Integer[] inhand);
}
