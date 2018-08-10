package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.InvOrderOut;
import com.xiaowei.worksystem.service.assets.IInvOrderOutService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class InvOrderOutServiceImpl extends BaseServiceImpl<InvOrderOut> implements IInvOrderOutService {

    public InvOrderOutServiceImpl(@Qualifier("invOrderOutRepository") BaseRepository repository) {
        super(repository);
    }

}