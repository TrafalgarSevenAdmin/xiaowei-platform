package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseSubjectRepository extends BaseRepository<ExpenseSubject>{

    @Query("select max(e.ownCode) from ExpenseSubject e where e.parentId = ?1")
    Long setMaxOwnCodeByParentId(String parentId);

}
