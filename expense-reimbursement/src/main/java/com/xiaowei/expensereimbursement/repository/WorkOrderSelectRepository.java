package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.WorkOrderSelect;
import org.springframework.data.jpa.repository.Query;

public interface WorkOrderSelectRepository extends BaseRepository<WorkOrderSelect>{

    @Query("select w from WorkOrderSelect w where w.code = ?1")
    WorkOrderSelect findByCode(String workOrderCode);

}
