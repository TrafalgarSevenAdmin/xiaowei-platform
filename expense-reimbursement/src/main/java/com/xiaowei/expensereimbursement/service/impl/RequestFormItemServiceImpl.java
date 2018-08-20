package com.xiaowei.expensereimbursement.service.impl;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.expensereimbursement.entity.RequestFormItem;
import com.xiaowei.expensereimbursement.repository.RequestFormItemRepository;
import com.xiaowei.expensereimbursement.service.IRequestFormItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class RequestFormItemServiceImpl extends BaseServiceImpl<RequestFormItem> implements IRequestFormItemService {

    @Autowired
    private RequestFormItemRepository requestFormItemRepository;

    public RequestFormItemServiceImpl(@Qualifier("requestFormItemRepository") BaseRepository repository) {
        super(repository);
    }

}
