package com.xiaowei.commonupload.service;


import com.xiaowei.commonupload.entity.FileStore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description
 * @Version 1.0
 */
public interface IFileStoreService  {

    FileStore findById(Serializable id);

    List<FileStore> findByIdIn(Set<String> ids);

    void delete(Serializable id);

    FileStore save(FileStore fileStore);

    List<FileStore> findByIds(List<Serializable> ids);

    List<String> getCheckIds(String table,String field);

    void updateCheckDate(List<String> ids, Date checkDate);

    void deleteFileStoreByCheckDate(Date currentDate);
}
