package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.ReimbursementStandard;
import com.xiaowei.expensereimbursement.repository.ReimbursementStandardRepository;
import com.xiaowei.expensereimbursement.service.IReimbursementStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ReimbursementStandardServiceImpl extends BaseServiceImpl<ReimbursementStandard> implements IReimbursementStandardService {

    @Autowired
    private ReimbursementStandardRepository reimbursementStandardRepository;


    public ReimbursementStandardServiceImpl(@Qualifier("reimbursementStandardRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ReimbursementStandard saveReimbursementStandard(ReimbursementStandard reimbursementStandard) {
        //判定参数是否合规
        judgeAttribute(reimbursementStandard, JudgeType.INSERT);
        reimbursementStandardRepository.save(reimbursementStandard);
        return reimbursementStandard;
    }

    @Override
    @Transactional
    public void deleteReimbursementStandard(String standardId) {
        EmptyUtils.assertString(standardId, "没有传入对象id");
        Optional<ReimbursementStandard> optional = reimbursementStandardRepository.findById(standardId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的对象");
        reimbursementStandardRepository.delete(optional.get());
    }

    @Override
    @Transactional
    public ReimbursementStandard updateReimbursementStandard(ReimbursementStandard reimbursementStandard) {
        //判定参数是否合规
        judgeAttribute(reimbursementStandard, JudgeType.UPDATE);
        reimbursementStandardRepository.save(reimbursementStandard);

        return reimbursementStandard;
    }

    @Override
    public List<String> findShipLevelByPostLevel(String postLevel, String subjectCode) {
        return reimbursementStandardRepository.findShipLevelByPostLevel(postLevel, subjectCode);
    }

    private void judgeAttribute(ReimbursementStandard reimbursementStandard, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            reimbursementStandard.setId(null);
            reimbursementStandard.setCreatedTime(new Date());
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String reimbursementStandardId = reimbursementStandard.getId();
            EmptyUtils.assertString(reimbursementStandardId, "没有传入对象id");
            Optional<ReimbursementStandard> optional = reimbursementStandardRepository.findById(reimbursementStandardId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");

        }
    }
}
