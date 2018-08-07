package com.xiaowei.pay.service;


import com.xiaowei.core.basic.service.IBaseService;
import com.xiaowei.pay.entity.XwOrder;

import java.io.Serializable;
import java.util.Optional;

public interface IOrderService extends IBaseService<XwOrder> {

    Optional<XwOrder> getById(Serializable id);
}
