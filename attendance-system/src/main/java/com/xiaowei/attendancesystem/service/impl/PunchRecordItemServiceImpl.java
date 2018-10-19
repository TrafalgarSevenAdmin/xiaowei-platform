package com.xiaowei.attendancesystem.service.impl;

import com.xiaowei.attendancesystem.entity.PunchRecordItem;
import com.xiaowei.attendancesystem.repository.PunchRecordItemRepository;
import com.xiaowei.attendancesystem.service.IPunchRecordItemService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PunchRecordItemServiceImpl extends BaseServiceImpl<PunchRecordItem> implements IPunchRecordItemService {

    @Autowired
    private PunchRecordItemRepository punchRecordItemRepository;

    public PunchRecordItemServiceImpl(@Qualifier("punchRecordItemRepository") BaseRepository repository) {
        super(repository);
    }

}
