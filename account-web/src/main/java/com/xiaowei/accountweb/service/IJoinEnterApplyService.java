package com.xiaowei.accountweb.service;

import com.xiaowei.accountweb.dto.JoinAuditDto;
import com.xiaowei.accountweb.entity.JoinEnterApply;
import com.xiaowei.core.basic.service.IBaseService;

public interface IJoinEnterApplyService extends IBaseService<JoinEnterApply> {
    void audit(JoinAuditDto joinAuditDto);
}
