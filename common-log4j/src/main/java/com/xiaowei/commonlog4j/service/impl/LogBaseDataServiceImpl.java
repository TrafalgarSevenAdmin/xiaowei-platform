package com.xiaowei.commonlog4j.service.impl;

import com.xiaowei.commonlog4j.entity.LogBaseData;
import com.xiaowei.commonlog4j.repository.LogBaseDataRepository;
import com.xiaowei.commonlog4j.service.ILogBaseDataService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description 权限服务
 * @Version 1.0
 */
@Service
public class LogBaseDataServiceImpl extends BaseServiceImpl<LogBaseData> implements ILogBaseDataService {

    @Autowired
    private LogBaseDataRepository logBaseDataRepository;

    public LogBaseDataServiceImpl(@Qualifier("logBaseDataRepository")BaseRepository repository) {
        super(repository);
    }


}
