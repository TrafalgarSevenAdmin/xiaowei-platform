package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.TenementStatus;
import com.xiaowei.account.entity.Tenement;
import com.xiaowei.account.repository.TenementRepository;
import com.xiaowei.account.service.ITenementService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 租户服务
 */
@Service
public class TenementServiceImpl extends BaseServiceImpl<Tenement> implements ITenementService {

    @Autowired
    private TenementRepository tenementRepository;

    public TenementServiceImpl(@Qualifier("tenementRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Tenement saveTenement(Tenement tenement) {
        //判定参数是否合规
        judgeAttribute(tenement, JudgeType.INSERT);
        tenementRepository.save(tenement);
        return tenement;
    }

    @Override
    @Transactional
    public Tenement updateTenement(Tenement tenement) {
        //判定参数是否合规
        judgeAttribute(tenement, JudgeType.UPDATE);
        tenementRepository.save(tenement);
        return tenement;
    }

    private void judgeAttribute(Tenement tenement, JudgeType judgeType) {
        //判断code是否唯一
        String code = tenement.getCode();
        if (StringUtils.isEmpty(code)) {
            throw new BusinessException("保存失败:编码为空");
        }

        if (judgeType.equals(JudgeType.INSERT)) {//保存
            tenement.setId(null);
            Tenement byCode = tenementRepository.findByCode(code);
            if (byCode != null) {
                throw new BusinessException("保存失败:租户名称重复");
            }
            //默认状态正常
            tenement.setStatus(TenementStatus.NORMAL.getStatus());

        } else if (judgeType.equals(JudgeType.UPDATE)) {//修改
            String tenementId = tenement.getId();

            if (StringUtils.isEmpty(tenementId)) {
                throw new BusinessException("保存失败:没有传入对象id");
            }
            Optional<Tenement> optional = tenementRepository.findById(tenementId);

            EmptyUtils.assertOptional(optional,"保存失败:没有查询到需要修改的对象");
            Tenement one = optional.get();

            //如果名称没有修改,则不发sql验证,否则发送sql验证name唯一性
            if (!one.getCode().equals(code)) {
                Tenement byCode = tenementRepository.findByCode(code);
                if (byCode != null) {
                    throw new BusinessException("保存失败:租户编码重复");
                }
            }
            //设置不允许在此处修改的属性
            tenement.setStatus(one.getStatus());

        }
    }

    @Override
    @Transactional
    public Tenement updateStatus(Tenement tenement) {
        String tenementId = tenement.getId();
        if (StringUtils.isEmpty(tenementId)) {
            throw new BusinessException("删除失败:没有传入对象id");
        }
        Optional<Tenement> optional = tenementRepository.findById(tenementId);
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        Tenement one = optional.get();
        one.setStatus(tenement.getStatus());
        tenementRepository.save(one);
        return one;
    }
}
