package com.xiaowei.commonupload.repository;

import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface FileStoreRepository extends BaseRepository<FileStore> {

    @Query("select fs from FileStore fs where fs.id in :ids")
    List<FileStore> findByIdIn(@Param("ids") Set<String> ids);

    @Modifying
    @Query("update FileStore f set f.checkDate = ?2 where f.id in ?1")
    void updateCheckDate(List<String> ids, Date checkDate);

    @Modifying
    @Query("delete from FileStore f where f.checkDate is null or f.checkDate <> ?1")
    void deleteFileStoreByCheckDate(Date currentDate);
}
