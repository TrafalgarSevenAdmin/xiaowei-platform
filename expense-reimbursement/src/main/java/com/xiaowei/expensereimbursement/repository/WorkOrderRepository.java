package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.WorkOrder;
import org.springframework.data.jpa.repository.Query;

public interface WorkOrderRepository extends BaseRepository<WorkOrder>{

    @Query("select w from WorkOrder w where w.code = ?1")
    WorkOrder findByCode(String workOrderCode);
}
