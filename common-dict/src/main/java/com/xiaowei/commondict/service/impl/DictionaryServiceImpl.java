package com.xiaowei.commondict.service.impl;

import com.xiaowei.commondict.entity.Dictionary;
import com.xiaowei.commondict.repository.DictionaryRepository;
import com.xiaowei.commondict.service.IDictionaryService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements IDictionaryService {
    @Autowired
    private DictionaryRepository dictionaryRepository;

    public DictionaryServiceImpl(@Qualifier("dictionaryRepository") BaseRepository<Dictionary> repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Dictionary saveDictionary(Dictionary dictionary) {
        //判定参数是否合规
        judgeAttribute(dictionary, JudgeType.INSERT);
        dictionaryRepository.save(dictionary);
        return dictionary;
    }

    private void judgeAttribute(Dictionary dictionary, JudgeType judgeType) {
        if(judgeType.equals(JudgeType.INSERT)){//保存
            dictionary.setId(null);
            //1.判断有没有parentId
            String parentId = dictionary.getParentId();
            if (StringUtils.isEmpty(parentId)) {
                dictionary.setLevel(1);
                dictionary.setCode(dictionary.getOwnCode());
            } else {
                Optional<Dictionary> parent = dictionaryRepository.findById(parentId);
                EmptyUtils.assertOptional(parent, "没有查询到父级字典");
                Dictionary parentDictionary = parent.get();
                dictionary.setLevel(parentDictionary.getLevel() + 1);
                dictionary.setCode(parentDictionary.getCode() + "_" + dictionary.getOwnCode());
            }
            dictionary.setCreatedTime(new Date());
        }else if(judgeType.equals(JudgeType.UPDATE)){//修改
            String dictionaryId = dictionary.getId();
            EmptyUtils.assertString(dictionaryId, "没有传入对象id");
            Optional<Dictionary> one = dictionaryRepository.findById(dictionaryId);
            EmptyUtils.assertOptional(one, "没有查询到需要修改的对象");
            //只能修改字典名称
            setUpdateDictionaryAttr(dictionary,one.get());
        }
    }

    /**
     * 设置字典不允许修改的字段
     * @param dictionary
     * @param oldDictionary
     */
    private void setUpdateDictionaryAttr(Dictionary dictionary, Dictionary oldDictionary) {
        dictionary.setCreatedTime(oldDictionary.getCreatedTime());
        dictionary.setCode(oldDictionary.getCode());
        dictionary.setOwnCode(oldDictionary.getOwnCode());
        dictionary.setLevel(oldDictionary.getLevel());
        dictionary.setParentId(oldDictionary.getParentId());
    }

    @Override
    @Transactional
    public Dictionary updateDictionary(Dictionary dictionary) {
        //判定参数是否合规
        judgeAttribute(dictionary, JudgeType.UPDATE);
        dictionaryRepository.save(dictionary);
        return dictionary;
    }

    @Override
    @Transactional
    public void deleteDictionary(String dictionaryId) {
        EmptyUtils.assertString(dictionaryId, "删除失败:没有传入对象id");
        Optional<Dictionary> one = dictionaryRepository.findById(dictionaryId);
        EmptyUtils.assertOptional(one, "删除失败:没有查询到需要删除的对象");
        Dictionary dictionary = one.get();
        dictionaryRepository.deleteByCodeLike(dictionary.getCode() + "%");
    }

}
