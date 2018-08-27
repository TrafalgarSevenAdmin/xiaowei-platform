package com.xiaowei.worksystem.service.assets;

import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.worksystem.entity.assets.InvOrderIn;

public interface IInvOrderInService extends IBaseService<InvOrderIn> {

    /**
     * 处理入库调拨单
     * @param invOrderIn
     */
    void handleInvOrderIn(InvOrderIn invOrderIn);
}