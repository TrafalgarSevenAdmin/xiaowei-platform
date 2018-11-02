package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.RequestForm;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RequestFormRepository extends BaseRepository<RequestForm>{

    @Query(value = "select count(*) from e_requestform_trial where " +
            "REQUESTFORM_ID = ?1 and TRIAL_ID = ?2",nativeQuery = true)
    Long findCountTrial(String requestFormId, String trialId);

    @Query(value = "select count(*) from E_REQUESTFORM er left join e_requestform_trial ert on " +
            "er.id = ert.REQUESTFORM_ID where ert.TRIAL_ID = ?1 and er.status = ?2",nativeQuery = true)
    Long findPreRequestCountCount(String userId, Integer status);

    @Query("select count(rf) from RequestForm rf where rf.audit.id = ?1")
    Long findRequestCountCount(String userId);

    @Query("select r from RequestForm r where r.workOrderCode = ?1")
    List<RequestForm> findByWorkOrderCode(String workOrderCode);

    @Query("select r from RequestForm r where r.code in ?1")
    List<RequestForm> findByIdIn(Set<String> codes);
}
