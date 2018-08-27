package com.xiaowei.worksystem.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.WorkOrder;
import org.springframework.data.jpa.repository.Query;

public interface WorkOrderRepository extends BaseRepository<WorkOrder>{

    @Query("select count(w) from WorkOrder w where w.engineer.id = ?1 and w.systemStatus in ?2")
    Long findCountByEngineerIdAndStatusIn(String engineerId, Integer[] inhand);

    @Query("select count(w) from WorkOrder w where w.proposer.id = ?1 and w.userStatus = ?2")
    Long findCountByProposerIdAndUserStatus(String userId, Integer status);

    @Query("select count(w) from WorkOrder w where w.backgrounder.id = ?1 and w.systemStatus = ?2")
    Long findCountByBackgrounderAndStatus(String userId, Integer status);

    @Query("select count(w) from WorkOrder w where w.backgrounder.id = ?1 and w.systemStatus in ?2")
    Long findCountByBackgrounderAndStatusIn(String userId, Integer[] finished);

}
