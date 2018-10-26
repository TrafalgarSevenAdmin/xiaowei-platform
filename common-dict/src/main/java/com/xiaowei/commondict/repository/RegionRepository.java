package com.xiaowei.commondict.repository;

import com.xiaowei.commondict.entity.Region;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegionRepository extends BaseRepository<Region>{

    /**
     * 根据level查询region
     * @param level
     * @return
     */
    @Query("select r from Region r where r.level = :level ")
    List<Region> findByLevel(@Param("level")Integer level);

    /**
     * 根据shortCode查询region
     * @param shortCode
     * @return
     */
    @Query("select r from Region r where r.shortCode = :shortCode ")
    Region findByShortCode(@Param("shortCode") String shortCode);

    /**
     * 根据shortCode删除region
     * @param shortCode
     */
    @Query("delete from Region r where r.shortCode = :shortCode")
    void deleteByShortCode(@Param("shortCode") String shortCode);

    @Query("select r from Region r where r.code = :code ")
    Region findByCode(@Param("code")String code);

    @Query("select r from Region r where r.parentShortCode=:code")
    List<Region> findChildrenByParentCode(@Param("code")String code);

    @Modifying
    @Query("delete from Region r where r.shortCode like :shortCodeLike")
    void deleteRegion(@Param("shortCodeLike") String shortCodeLike);

    @Modifying
    @Query("update Region set cityLevel =:cityLevel where shortCode like :code")
    void updateCityLevel(@Param("code") String code, @Param("cityLevel") String cityLevel);
}
