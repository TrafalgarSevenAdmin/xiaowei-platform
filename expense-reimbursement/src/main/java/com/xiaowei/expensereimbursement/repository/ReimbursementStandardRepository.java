package com.xiaowei.expensereimbursement.repository;

import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.expensereimbursement.entity.ReimbursementStandard;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReimbursementStandardRepository extends BaseRepository<ReimbursementStandard>{
    @Query("select r.shipLevel from ReimbursementStandard r where r.postLevel = ?1 and r.subjectCode = ?2")
    List<String> findShipLevelByPostLevel(String postLevel, String subjectCode);

    @Query("select r.cityLevel from ReimbursementStandard r where r.postLevel = ?1 and r.subjectCode = ?2")
    List<String> findCityLevelByPostLevel(String postLevel, String subjectCode);
}
