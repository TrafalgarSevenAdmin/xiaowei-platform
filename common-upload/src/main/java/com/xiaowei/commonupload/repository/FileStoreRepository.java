package com.xiaowei.commonupload.repository;

import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface FileStoreRepository extends BaseRepository<FileStore> {

    @Query("select fs from FileStore fs where fs.id in :ids")
    List<FileStore> findByIdIn(@Param("ids") Set<String> ids);
}
