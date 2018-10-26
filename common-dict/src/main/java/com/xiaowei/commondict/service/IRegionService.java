package com.xiaowei.commondict.service;


import com.xiaowei.commondict.entity.Region;
import com.xiaowei.core.basic.service.IBaseService;

import java.util.List;

/**
 * @author yaohuaiying
 * @Date 2017-09-01  13:43
 * @Description 行政区域服务
 * @Version 1.0
 */
public interface IRegionService extends IBaseService<Region> {

    /**
     * 根据code查询ChildrenRegions
     * @param code
     * @return
     */
    List<Region> findChildrenByParentCode(String code);


    /**
     * 根据等级查询COUNTRY("国家"), PROVINCE("省"), CITY("市"), COUNTY("县"), TOWN("镇"), VILLAGE("村"), GROUP("组")
     * @param level
     * @return
     */
    List<Region> findByLevel(Integer level);

    /**
     * 根据shortCode查询region
     * @param shortCode
     * @return
     */
    Region findByShortCode(String shortCode);

    /**
     * 根据shortCode删除region
     * @param shortCode
     */
    void deleteByShortByCode(String shortCode);

    Region findByCode(String code);

    Region saveRegion(Region region);

    void deleteRegion(String id);

    Region updateRegion(Region region);

    void updateCityLevel(String code, String cityLevel);
}
