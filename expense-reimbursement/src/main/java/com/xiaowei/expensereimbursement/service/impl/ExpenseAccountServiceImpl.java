package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.ExpenseAccount;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import com.xiaowei.expensereimbursement.repository.ExpenseAccountRepository;
import com.xiaowei.expensereimbursement.repository.ExpenseSubjectRepository;
import com.xiaowei.expensereimbursement.service.IExpenseAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
public class ExpenseAccountServiceImpl extends BaseServiceImpl<ExpenseAccount> implements IExpenseAccountService {

    @Autowired
    private ExpenseAccountRepository expenseAccountRepository;
    @Autowired
    private ExpenseSubjectRepository expenseSubjectRepository;


    public ExpenseAccountServiceImpl(@Qualifier("expenseAccountRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ExpenseAccount saveExpenseAccount(ExpenseAccount expenseAccount) {
        //判定参数是否合规
        judgeAttribute(expenseAccount, JudgeType.INSERT);
        expenseAccountRepository.save(expenseAccount);
        return expenseAccount;
    }

    private void judgeAttribute(ExpenseAccount expenseAccount, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            expenseAccount.setId(null);
            expenseAccount.setCreatedTime(new Date());
        }else if(judgeType.equals(JudgeType.UPDATE)){//修改
            String expenseAccountId = expenseAccount.getId();
            EmptyUtils.assertString(expenseAccountId, "没有传入对象id");
            Optional<ExpenseAccount> optional = expenseAccountRepository.findById(expenseAccountId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        }
        //验证所属一级科目
        ExpenseSubject expenseSubject = expenseAccount.getExpenseSubject();
        EmptyUtils.assertObject(expenseSubject,"所属一级科目必填");
        EmptyUtils.assertString(expenseSubject.getId(),"所属一级科目id必填");
        Optional<ExpenseSubject> subjectOptional = expenseSubjectRepository.findById(expenseSubject.getId());
        EmptyUtils.assertOptional(subjectOptional,"没有查询到所属一级科目");
    }

    @Override
    @Transactional
    public ExpenseAccount updateExpenseAccount(ExpenseAccount expenseAccount) {
        //判定参数是否合规
        judgeAttribute(expenseAccount, JudgeType.UPDATE);
        expenseAccountRepository.save(expenseAccount);
        return expenseAccount;
    }

}
