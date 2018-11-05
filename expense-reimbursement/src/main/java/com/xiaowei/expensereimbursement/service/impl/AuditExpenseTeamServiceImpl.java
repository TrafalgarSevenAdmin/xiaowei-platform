package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeam;
import com.xiaowei.expensereimbursement.entity.AuditExpenseTeamItem;
import com.xiaowei.expensereimbursement.repository.AuditExpenseTeamItemRepository;
import com.xiaowei.expensereimbursement.repository.AuditExpenseTeamRepository;
import com.xiaowei.expensereimbursement.service.IAuditExpenseTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class AuditExpenseTeamServiceImpl extends BaseServiceImpl<AuditExpenseTeam> implements IAuditExpenseTeamService {

    @Autowired
    private AuditExpenseTeamRepository auditExpenseTeamRepository;
    @Autowired
    private AuditExpenseTeamItemRepository auditExpenseTeamItemRepository;


    public AuditExpenseTeamServiceImpl(@Qualifier("auditExpenseTeamRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public AuditExpenseTeam saveAuditExpenseTeam(AuditExpenseTeam auditExpenseTeam) {
        //判定参数是否合规
        judgeAttribute(auditExpenseTeam, JudgeType.INSERT);
        auditExpenseTeamRepository.save(auditExpenseTeam);
        saveAuditExpenseTeamItems(auditExpenseTeam);//保存明细
        return auditExpenseTeam;
    }

    private void saveAuditExpenseTeamItems(AuditExpenseTeam auditExpenseTeam) {
        List<AuditExpenseTeamItem> auditExpenseTeamItems = auditExpenseTeam.getAuditExpenseTeamItems();
        if (CollectionUtils.isEmpty(auditExpenseTeamItems)) {
            return;
        }
        for (int i = 0; i < auditExpenseTeamItems.size(); i++) {
            AuditExpenseTeamItem auditExpenseTeamItem = auditExpenseTeamItems.get(i);
            auditExpenseTeamItem.setAuditExpenseTeam(auditExpenseTeam);
            auditExpenseTeamItem.setOrderNumber(i + 1);
            auditExpenseTeamItemRepository.save(auditExpenseTeamItem);
        }
    }

    private void judgeAttribute(AuditExpenseTeam auditExpenseTeam, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            auditExpenseTeam.setId(null);
            auditExpenseTeam.setCode(StringPYUtils.getSpellCode(auditExpenseTeam.getTeamName()));
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String auditExpenseTeamId = auditExpenseTeam.getId();
            EmptyUtils.assertString(auditExpenseTeamId, "没有传入对象id");
            Optional<AuditExpenseTeam> optional = auditExpenseTeamRepository.findById(auditExpenseTeamId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
        }
    }

    @Override
    @Transactional
    public AuditExpenseTeam updateAuditExpenseTeam(AuditExpenseTeam auditExpenseTeam) {
        //判定参数是否合规
        judgeAttribute(auditExpenseTeam, JudgeType.UPDATE);
        auditExpenseTeamRepository.save(auditExpenseTeam);
        deleteAuditExpenseTeamItems(auditExpenseTeam.getId());//删除明细
        saveAuditExpenseTeamItems(auditExpenseTeam);//保存明细
        return auditExpenseTeam;
    }

    @Override
    @Transactional
    public void deleteAuditExpenseTeam(String teamId) {
        EmptyUtils.assertString(teamId, "没有传入对象id");
        Optional<AuditExpenseTeam> optional = auditExpenseTeamRepository.findById(teamId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的对象");
        AuditExpenseTeam auditExpenseTeam = optional.get();
        //先删除明细
        deleteAuditExpenseTeamItems(teamId);
        //再删除小组
        auditExpenseTeamRepository.delete(auditExpenseTeam);
    }

    private void deleteAuditExpenseTeamItems(String teamId) {
        auditExpenseTeamItemRepository.deleteByTeamId(teamId);
    }

}
