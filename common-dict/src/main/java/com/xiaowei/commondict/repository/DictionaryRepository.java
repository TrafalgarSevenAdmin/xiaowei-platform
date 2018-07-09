package com.xiaowei.commondict.repository;

import com.xiaowei.commondict.entity.Dictionary;
import com.xiaowei.core.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DictionaryRepository extends BaseRepository<Dictionary>{

    @Modifying
    @Query("delete from Dictionary d where d.code like :codeLike")
    void deleteByCodeLike(@Param("codeLike") String codeLike);
}
