package com.xiaowei.commondict.service;

import com.xiaowei.commondict.entity.Dictionary;
import com.xiaowei.core.basic.service.IBaseService;

public interface IDictionaryService extends IBaseService<Dictionary>{
    Dictionary saveDictionary(Dictionary dictionary);

    Dictionary updateDictionary(Dictionary dictionary);

    void deleteDictionary(String dictionaryId);
}
