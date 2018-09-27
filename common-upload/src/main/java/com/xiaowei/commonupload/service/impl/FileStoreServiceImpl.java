package com.xiaowei.commonupload.service.impl;

import com.xiaowei.commonupload.asyn.DeleteFileStoreThread;
import com.xiaowei.commonupload.entity.FileStore;
import com.xiaowei.commonupload.repository.FileStoreRepository;
import com.xiaowei.commonupload.service.IFileStoreService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author mocker
 * @Date 2018-03-20 14:49:59
 * @Description
 * @Version 1.0
 */
@Service
public class FileStoreServiceImpl extends BaseServiceImpl<FileStore> implements IFileStoreService {

    private FileStoreRepository fileStoreRepository;

    public FileStoreServiceImpl(@Qualifier("fileStoreRepository") BaseRepository baseRepository) {
        super(baseRepository);
        this.fileStoreRepository = (FileStoreRepository) baseRepository;
    }

    @Cacheable(value = "fileStore", key = "#id")
    @Override
    public FileStore findById(Serializable id) {
        Optional<FileStore> byId = fileStoreRepository.findById(id);
        return byId.isPresent() ? byId.get() : null;
    }

    @CacheEvict(value = "fileStore", key = "#id")
    @Override
    public void delete(Serializable id) {
        fileStoreRepository.deleteById(id);
    }

    public FileStore save(FileStore fileStore) {
        return fileStoreRepository.save(fileStore);
    }

    @Override
    public List<String> getCheckIds(String table, String field) {
        val sql = "select " + field + " from " + table;

        return getEntityManager().createNativeQuery(sql).getResultList();
    }

    @Override
    @Transactional
    public void updateCheckDate(List<String> ids, Date checkDate) {
        fileStoreRepository.updateCheckDate(ids, checkDate);
    }

    @Override
    @Transactional
    public void deleteFileStoreByCheckDate(Date currentDate) {
        List<FileStore> fileStores = fileStoreRepository.findByCheckDate(currentDate);
        if(CollectionUtils.isEmpty(fileStores)){
            return;
        }
        new Thread(new DeleteFileStoreThread(fileStores)).start();
    }

    @Override
    public List<FileStore> findByIdIn(Set<String> ids) {
        return fileStoreRepository.findByIdIn(ids);
    }
}
