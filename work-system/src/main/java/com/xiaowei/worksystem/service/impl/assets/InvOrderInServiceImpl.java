package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;
import com.xiaowei.worksystem.repository.assets.InvOrderInRepository;
import com.xiaowei.worksystem.service.assets.IInvOrderInService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class InvOrderInServiceImpl extends BaseServiceImpl<InvOrderIn> implements IInvOrderInService {

    public InvOrderInServiceImpl(@Qualifier("invOrderInRepository") InvOrderInRepository repository) {
        super(repository);
    }

}