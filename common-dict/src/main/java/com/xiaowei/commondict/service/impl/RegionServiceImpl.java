package com.xiaowei.commondict.service.impl;

import com.xiaowei.commondict.entity.Region;
import com.xiaowei.commondict.region.RegionLevelEnum;
import com.xiaowei.commondict.region.RegionUtils;
import com.xiaowei.commondict.repository.RegionRepository;
import com.xiaowei.commondict.service.IRegionService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author yaohuaiying
 * @Date 2017-09-01  13:43
 * @Description 行政区域服务
 * @Version 1.0
 */
@Service
public class RegionServiceImpl extends BaseServiceImpl<Region> implements IRegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionServiceImpl(@Qualifier("regionRepository") BaseRepository repository) {
        super(repository);
    }


    @Override
    public List<Region> findChildrenByParentCode(String code) {
        code = RegionUtils.completed(code);
        Region region = findByCode(code);
        if (region == null) {
            return null;
        }
        List<Region> regions = regionRepository.findChildrenByParentCode(region.getShortCode());
        return regions;
    }

    @Override
    public List<Region> findByLevel(Integer level) {
        return regionRepository.findByLevel(level);
    }

    @Override
    public Region findByShortCode(String shortCode) {
        Region region = regionRepository.findByShortCode(shortCode);
        return region;
    }

    @Override
    public void deleteByShortByCode(String shortCode) {
        regionRepository.deleteByShortCode(shortCode);
    }

    @Override
    public Region findByCode(String code) {
        return regionRepository.findByCode(code);
    }

    @Override
    @Transactional
    public Region saveRegion(Region region) {
        //验证
        validateRegion(region);

        regionRepository.save(region);
        return region;
    }

    private void validateRegion(Region region) {
        Integer level = region.getLevel();
        Integer length = region.getShortCode().length();
        RegionLevelEnum regionLevelEnum1 = Arrays.stream(RegionLevelEnum.values()).filter(regionLevelEnum -> regionLevelEnum.getLevel().equals(level)).findAny().get();
        if(!regionLevelEnum1.getLength().equals(length)){
            throw new RuntimeException("验证不通过:编码长度不正确!");
        }
    }

    @Override
    @Transactional
    public void deleteRegion(String id) {
        Optional<Region> optional = regionRepository.findById(id);
        EmptyUtils.assertOptional(optional,"没有查询到需要删除的区域");

        String shortCode = optional.get().getShortCode();
        regionRepository.deleteRegion(shortCode + "%");
    }

    @Override
    @Transactional
    public Region updateRegion(Region region) {
        //验证
        validateRegion(region);

        regionRepository.save(region);
        return region;
    }

    @Override
    @Transactional
    public void updateCityLevel(String code, String cityLevel) {
        regionRepository.updateCityLevel(code, cityLevel);
    }

}
