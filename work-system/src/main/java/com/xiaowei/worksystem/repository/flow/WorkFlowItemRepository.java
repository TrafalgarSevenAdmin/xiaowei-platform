package com.xiaowei.worksystem.repository.flow;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.worksystem.entity.flow.WorkFlowItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkFlowItemRepository extends BaseRepository<WorkFlowItem>{

    @Modifying
    @Query("delete from WorkFlowItem wfi where wfi.workFlowId = ?1")
    void deleteByWorkFlowId(String workFlowId);

    @Query("select wfi from WorkFlowItem wfi where wfi.workFlowId = ?1")
    List<WorkFlowItem> findByWorkFlowId(String workFlowId);

    List<WorkFlowItem> findByCode(String code);
}
