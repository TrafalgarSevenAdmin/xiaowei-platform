package com.xiaowei.worksystem.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.worksystem.entity.Evaluate;
import com.xiaowei.worksystem.repository.EvaluateRepository;
import com.xiaowei.worksystem.service.IEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class EvaluateServiceImpl extends BaseServiceImpl<Evaluate> implements IEvaluateService {

    @Autowired
    private EvaluateRepository evaluateRepository;

    public EvaluateServiceImpl(@Qualifier("evaluateRepository")BaseRepository repository) {
        super(repository);
    }


}
