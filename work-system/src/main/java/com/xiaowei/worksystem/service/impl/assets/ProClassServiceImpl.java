package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.ProClass;
import com.xiaowei.worksystem.service.assets.IProClassService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class ProClassServiceImpl extends BaseServiceImpl<ProClass> implements IProClassService {

    public ProClassServiceImpl(@Qualifier("proClassRepository") BaseRepository repository) {
        super(repository);
    }

}