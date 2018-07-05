package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.exception.BusinessException;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.worksystem.entity.Workpiece;
import com.xiaowei.worksystem.repository.WorkpieceRepository;
import com.xiaowei.worksystem.service.IWorkpieceService;
import com.xiaowei.worksystem.status.CommonStatus;
import com.xiaowei.worksystem.status.WorkpieceStoreType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


@Service
public class WorkpieceServiceImpl extends BaseServiceImpl<Workpiece> implements IWorkpieceService {

    @Autowired
    private WorkpieceRepository workpieceRepository;

    public WorkpieceServiceImpl(@Qualifier("workpieceRepository")BaseRepository repository) {
        super(repository);
    }


    /**
     * 在保存操作的时候,若发现此设备的编号已经存在，就提示已经存在了。
     * 若是工程师添加的坏件，就合并
     * @param workpiece
     * @return
     */
    @Override
    public Workpiece save(Workpiece workpiece){
        workpiece.setCreatedTime(new Date());
        String code = workpiece.getCode();
        switch (workpiece.getStoreType()) {
            case 0://工程师添加的坏件。
                //判断手头是否有此工件
                Workpiece byCode = workpieceRepository.findByCodeAndUserId(code, workpiece.getUserId());
                if (byCode != null) {
                    //如果工程师手头没有此工件，不处理，直接保存
                } else {
                    //若有，就认为是同一个坏件的二次提交。将坏件数量相加
                    workpiece.setBadTotal(workpiece.getBadTotal() + byCode.getBadTotal());
                }
                break;
            case 1://总仓库
                //后台添加的工件，可以是坏件或者好件
                //如果厂库中存在此条数据，就报错，否者保存
                byCode = workpieceRepository.findByCodeAndStoreType(code, WorkpieceStoreType.CORE.getStatus());
                EmptyUtils.assertObjectNotNull(byCode, "已存在此工件编号信息，请核对");
                break;
            default:
                throw new BusinessException("数据异常");
        }
        return workpieceRepository.save(workpiece);
    }

    /**
     * 伪删除
     * @param workpieceId
     */
    @Override
    public void fakeDelete(String workpieceId) {
        EmptyUtils.assertString(workpieceId, "没有传入对象id");
        Optional<Workpiece> one = workpieceRepository.findById(workpieceId);
        EmptyUtils.assertOptional(one, "没有查询到需要删除的对象");
        Workpiece Workpiece = one.get();
        Workpiece.setStatus(CommonStatus.DELETE.getStatus());
        workpieceRepository.save(Workpiece);
    }
}
