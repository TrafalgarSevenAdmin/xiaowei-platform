package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseFormRepository extends BaseRepository<ExpenseForm>{

    @Query(value = "select count(*) from e_expenseform_first_trial where " +
            "EXPENSEFORM_ID = ?1 and FIRSTTRIAL_ID = ?2",nativeQuery = true)
    Long findCountFirstTrial(String expenseFormId, String firstTrialId);

    @Query(value = "select count(*) from e_expenseform_second_trial where " +
            "EXPENSEFORM_ID = ?1 and SECONDTRIAL_ID = ?2",nativeQuery = true)
    Long findCountSecondTrial(String expenseFormId, String secondTrialId);
}
