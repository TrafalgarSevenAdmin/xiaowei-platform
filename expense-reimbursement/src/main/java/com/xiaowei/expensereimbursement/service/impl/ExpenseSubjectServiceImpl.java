package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.ExpenseSubject;
import com.xiaowei.expensereimbursement.repository.ExpenseSubjectRepository;
import com.xiaowei.expensereimbursement.service.IExpenseSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


@Service
public class ExpenseSubjectServiceImpl extends BaseServiceImpl<ExpenseSubject> implements IExpenseSubjectService {

    @Autowired
    private ExpenseSubjectRepository expenseSubjectRepository;


    public ExpenseSubjectServiceImpl(@Qualifier("expenseSubjectRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ExpenseSubject saveExpenseSubject(ExpenseSubject expenseSubject) {
        //判定参数是否合规
        judgeAttribute(expenseSubject, JudgeType.INSERT);
        expenseSubjectRepository.save(expenseSubject);
        return expenseSubject;
    }

    private void judgeAttribute(ExpenseSubject expenseSubject, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            expenseSubject.setId(null);
            expenseSubject.setCreatedTime(new Date());
        }else if(judgeType.equals(JudgeType.UPDATE)){//修改
            String expenseSubjectId = expenseSubject.getId();
            EmptyUtils.assertString(expenseSubjectId, "没有传入对象id");
            Optional<ExpenseSubject> optional = expenseSubjectRepository.findById(expenseSubjectId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        }
    }

    @Override
    @Transactional
    public ExpenseSubject updateExpenseSubject(ExpenseSubject expenseSubject) {
        //判定参数是否合规
        judgeAttribute(expenseSubject, JudgeType.UPDATE);
        expenseSubjectRepository.save(expenseSubject);
        return expenseSubject;
    }
}
