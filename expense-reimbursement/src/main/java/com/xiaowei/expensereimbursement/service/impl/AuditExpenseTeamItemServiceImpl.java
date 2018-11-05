package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeamItem;
import com.xiaowei.expensereimbursement.repository.AuditExpenseTeamItemRepository;
import com.xiaowei.expensereimbursement.service.IAuditExpenseTeamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class AuditExpenseTeamItemServiceImpl extends BaseServiceImpl<AuditExpenseTeamItem> implements IAuditExpenseTeamItemService {

    @Autowired
    private AuditExpenseTeamItemRepository auditExpenseTeamItemRepository;


    public AuditExpenseTeamItemServiceImpl(@Qualifier("auditExpenseTeamItemRepository") BaseRepository repository) {
        super(repository);
    }

}
