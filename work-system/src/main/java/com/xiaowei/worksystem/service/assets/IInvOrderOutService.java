package com.xiaowei.worksystem.service.assets;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.assets.InvOrderOut;

public interface IInvOrderOutService extends IBaseService<InvOrderOut> {

    /**
     * 处理出库调拨单
     * @param invOrderOut
     */
    void handleInvOrderOut(InvOrderOut invOrderOut);
}