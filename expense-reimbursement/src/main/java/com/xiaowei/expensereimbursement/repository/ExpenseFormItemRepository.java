package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.ExpenseFormItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseFormItemRepository extends BaseRepository<ExpenseFormItem>{

    @Modifying
    @Query("delete from ExpenseFormItem e where e.expenseForm.id = ?1")
    void deleteByExpenseFormId(String expenseFormId);
}
