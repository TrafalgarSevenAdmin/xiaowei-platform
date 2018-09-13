package com.xiaowei.pay.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.pay.entity.XwOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface XwOrderRepository extends BaseRepository<XwOrder> {

    @Query("select x from XwOrder x where x.businessId = ?1 and x.xwType = ?2 order by x.timeExpire desc ")
    List<XwOrder> findByBusinessIdAndType(String workOrderId, Integer xwType);
}
