package com.xiaowei.attendancesystem.service.impl;

import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.attendancesystem.repository.ChiefEngineerRepository;
import com.xiaowei.attendancesystem.service.IChiefEngineerService;
import com.xiaowei.attendancesystem.status.ChiefEngineerStatus;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class ChiefEngineerServiceImpl extends BaseServiceImpl<ChiefEngineer> implements IChiefEngineerService{

    @Autowired
    private ChiefEngineerRepository chiefEngineerRepository;

    public ChiefEngineerServiceImpl(@Qualifier("chiefEngineerRepository") BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public ChiefEngineer saveChiefEngineer(ChiefEngineer chiefEngineer) {
        //判定参数是否合规
        judgeAttribute(chiefEngineer, JudgeType.INSERT);
        chiefEngineerRepository.save(chiefEngineer);
        return chiefEngineer;
    }

    private void judgeAttribute(ChiefEngineer chiefEngineer, JudgeType judgeType) {
        if (judgeType.equals(JudgeType.INSERT)) {//保存
            chiefEngineer.setId(null);
            chiefEngineer.setCode(StringPYUtils.getSpellCode(chiefEngineer.getChiefName()));
            chiefEngineer.setCreatedTime(new Date());
            chiefEngineer.setStatus(ChiefEngineerStatus.NORMAL.getStatus());//默认状态正常
        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String chiefEngineerId = chiefEngineer.getId();
            EmptyUtils.assertString(chiefEngineerId, "没有传入对象id");
            Optional<ChiefEngineer> one = chiefEngineerRepository.findById(chiefEngineerId);
            EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
        }
    }

    @Override
    @Transactional
    public ChiefEngineer updateChiefEngineer(ChiefEngineer chiefEngineer) {
        //判定参数是否合规
        judgeAttribute(chiefEngineer, JudgeType.UPDATE);
        chiefEngineerRepository.save(chiefEngineer);
        return chiefEngineer;
    }

    @Override
    @Transactional
    public void fakeDeleteChiefEngineer(String chiefEngineerId) {
        EmptyUtils.assertString(chiefEngineerId,"删除失败:没有传入对象id");
        Optional<ChiefEngineer> optional = chiefEngineerRepository.findById(chiefEngineerId);
        EmptyUtils.assertOptional(optional,"没有查询到需要删除的对象");
        ChiefEngineer one = optional.get();
        one.setStatus(ChiefEngineerStatus.DELETE.getStatus());
        one.setDepartments(null);
        chiefEngineerRepository.save(one);
    }

    @Override
    @Transactional
    public ChiefEngineer updateStatus(ChiefEngineer chiefEngineer) {
        String chiefEngineerId = chiefEngineer.getId();
        EmptyUtils.assertString(chiefEngineerId,"没有传入对象id");
        Optional<ChiefEngineer> optional = chiefEngineerRepository.findById(chiefEngineerId);
        EmptyUtils.assertOptional(optional,"没有查询到需要删除的对象");
        ChiefEngineer one = optional.get();
        one.setStatus(chiefEngineer.getStatus());
        chiefEngineerRepository.save(one);
        return one;
    }
}
