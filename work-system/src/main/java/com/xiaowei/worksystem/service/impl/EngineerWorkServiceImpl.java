package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.EngineerWork;
import com.xiaowei.worksystem.repository.EngineerWorkRepository;
import com.xiaowei.worksystem.service.IEngineerWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * 待修改的设备服务
 */
@Service
public class EngineerWorkServiceImpl extends BaseServiceImpl<EngineerWork> implements IEngineerWorkService {

    @Autowired
    private EngineerWorkRepository engineerWorkRepository;

    public EngineerWorkServiceImpl(@Qualifier("engineerWorkRepository")BaseRepository repository) {
        super(repository);
    }

}
