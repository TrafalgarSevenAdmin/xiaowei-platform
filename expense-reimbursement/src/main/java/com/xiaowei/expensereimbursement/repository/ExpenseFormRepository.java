package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.ExpenseForm;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseFormRepository extends BaseRepository<ExpenseForm>{

    @Query(value = "select count(*) from e_expenseform_first_trial where " +
            "EXPENSEFORM_ID = ?1 and FIRSTTRIAL_ID = ?2",nativeQuery = true)
    Long findCountFirstTrial(String expenseFormId, String firstTrialId);

    @Query(value = "select count(*) from e_expenseform_second_trial where " +
            "EXPENSEFORM_ID = ?1 and SECONDTRIAL_ID = ?2",nativeQuery = true)
    Long findCountSecondTrial(String expenseFormId, String secondTrialId);

    @Query(value = "select count(*) from E_EXPENSEFORM e left join e_expenseform_first_trial ef on " +
            "e.id = ef.EXPENSEFORM_ID where ef.FIRSTTRIAL_ID = ?1 and e.status = ?2",nativeQuery = true)
    Long findFirstTrialCount(String userId, Integer status);

    @Query(value = "select count(*) from E_EXPENSEFORM e left join e_expenseform_second_trial ef on " +
            "e.id = ef.EXPENSEFORM_ID where ef.SECONDTRIAL_ID = ?1 and e.status = ?2",nativeQuery = true)
    Long findSecondTrialCount(String userId, Integer status);

    @Query("select count(e) from ExpenseForm e where e.firstAudit.id = ?1")
    Long findFirstAuditCount(String userId);

    @Query("select count(e) from ExpenseForm e where e.secondAudit.id = ?1")
    Long findSecondAuditCount(String userId);

    @Query("select count(e) from ExpenseForm e where e.expenseUser.id = ?1 and e.status in ?2")
    Long findByUserIdAndStatusIn(String userId, Integer[] integers);

    @Query("select count(e) from ExpenseForm e where e.expenseUser.id = ?1 and e.status = ?2")
    Long findByUserIdAndStatus(String userId, Integer status);

    @Query("select e from ExpenseForm e where e.workOrderCode = ?1 and e.id <> ?2")
    List<ExpenseForm> findByWorkOrderCodeAndNotId(String workOrderCode, String id);
}
