package com.xiaowei.flow.service.impl.auth;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.flow.entity.auth.AuthGrant;
import com.xiaowei.flow.service.auth.IAuthGrantService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class AuthGrantServiceImpl extends BaseServiceImpl<AuthGrant> implements IAuthGrantService {

    public AuthGrantServiceImpl(@Qualifier("authGrantRepository") BaseRepository repository) {
        super(repository);
    }

}