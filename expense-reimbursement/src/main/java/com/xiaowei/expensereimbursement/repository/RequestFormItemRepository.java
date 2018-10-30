package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.RequestFormItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestFormItemRepository extends BaseRepository<RequestFormItem>{

    @Modifying
    @Query("delete from RequestFormItem ri where ri.requestForm.id = ?1")
    void deleteByRequestFormId(String requestFormId);

    @Query("select ri from RequestFormItem ri where ri.requestForm.workOrderCode = ?1")
    List<RequestFormItem> findByWorkOrderCode(String workOrderCode);
}
