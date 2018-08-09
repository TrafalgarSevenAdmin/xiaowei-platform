package com.xiaowei.attendancesystem.service.impl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.attendancesystem.repository.ChiefEngineerRepository;
import com.xiaowei.attendancesystem.service.IChiefEngineerService;
import com.xiaowei.attendancesystem.status.ChiefEngineerStatus;
import com.xiaowei.commonjts.utils.CalculateUtils;
import com.xiaowei.commonjts.utils.GeometryUtil;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.utils.StringPYUtils;
import com.xiaowei.core.validate.JudgeType;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChiefEngineerServiceImpl extends BaseServiceImpl<ChiefEngineer> implements IChiefEngineerService {

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
            Optional<ChiefEngineer> optional = chiefEngineerRepository.findById(chiefEngineerId);
            EmptyUtils.assertOptional(optional, "没有查询到需要修改的对象");
            ChiefEngineer one = optional.get();
            //设置无法修改的字段
            chiefEngineer.setStatus(one.getStatus());//状态无法在此处修改
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
        EmptyUtils.assertString(chiefEngineerId, "删除失败:没有传入对象id");
        Optional<ChiefEngineer> optional = chiefEngineerRepository.findById(chiefEngineerId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的对象");
        chiefEngineerRepository.delete(optional.get());
    }

    @Override
    @Transactional
    public ChiefEngineer updateStatus(ChiefEngineer chiefEngineer) {
        String chiefEngineerId = chiefEngineer.getId();
        EmptyUtils.assertString(chiefEngineerId, "没有传入对象id");
        Optional<ChiefEngineer> optional = chiefEngineerRepository.findById(chiefEngineerId);
        EmptyUtils.assertOptional(optional, "没有查询到需要删除的对象");
        ChiefEngineer one = optional.get();
        one.setStatus(chiefEngineer.getStatus());
        chiefEngineerRepository.save(one);
        return one;
    }

    /**
     * 获取当前登录用户最近的办公点
     * @param geometry
     * @return
     */
    @Override
    public ChiefEngineer findNearest(Geometry geometry) {
        String loginUserId = LoginUserUtils.getLoginUser().getId();
        val chiefEngineers = chiefEngineerRepository.findByUserId(loginUserId).stream().filter(chiefEngineer ->
                ChiefEngineerStatus.NORMAL.getStatus() == chiefEngineer.getStatus()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(chiefEngineers)) {
            throw new BusinessException("没有查询到任何办公点");
        }
        ChiefEngineer nearestChief = null;
        Double nearest = null;
        for (ChiefEngineer chiefEngineer : chiefEngineers) {
            if (nearest == null) {
                nearest = CalculateUtils.GetDistance(GeometryUtil.getGps((Point) chiefEngineer.getShape()),
                        GeometryUtil.getGps((Point) geometry));
                nearestChief = chiefEngineer;
            } else {
                double v = CalculateUtils.GetDistance(GeometryUtil.getGps((Point) chiefEngineer.getShape()),
                        GeometryUtil.getGps((Point) geometry));
                if (v < nearest) {
                    nearest = v;
                    nearestChief = chiefEngineer;
                }
            }
        }

        return nearestChief;
    }
}
