package com.xiaowei.worksystem.service.impl.assets;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.assets.ProBrand;
import com.xiaowei.worksystem.service.assets.IProBrandService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class ProBrandServiceImpl extends BaseServiceImpl<ProBrand> implements IProBrandService {

    public ProBrandServiceImpl(@Qualifier("proBrandRepository") BaseRepository repository) {
        super(repository);
    }

}