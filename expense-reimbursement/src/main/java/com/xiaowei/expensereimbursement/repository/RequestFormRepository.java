package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.RequestForm;
import org.springframework.data.jpa.repository.Query;

public interface RequestFormRepository extends BaseRepository<RequestForm>{

    @Query(value = "select count(*) from e_requestform_trial where " +
            "REQUESTFORM_ID = ?1 and TRIAL_ID = ?2",nativeQuery = true)
    Long findCountTrial(String requestFormId, String trialId);
}
