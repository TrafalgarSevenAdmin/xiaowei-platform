package com.xiaowei.commondict.controller;

import com.xiaowei.commondict.dto.DictionaryDTO;
import com.xiaowei.commondict.entity.Dictionary;
import com.xiaowei.commondict.query.DictionaryQuery;
import com.xiaowei.commondict.service.IDictionaryService;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.tree.JsonTreeCreater;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "字典接口")
@RestController
@RequestMapping("/api/dict")
public class DictionaryController {
    @Autowired
    private IDictionaryService dictionaryService;

    @ApiOperation(value = "添加字典")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) DictionaryDTO dictionaryDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Dictionary dictionary = BeanCopyUtils.copy(dictionaryDTO, Dictionary.class);
        dictionary = dictionaryService.saveDictionary(dictionary);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(dictionary, fieldsView));
    }


    @ApiOperation(value = "修改字典")
    @AutoErrorHandler
    @PutMapping("/{dictionaryId}")
    public Result update(@PathVariable("dictionaryId") String dictionaryId, @RequestBody @Validated(V.Update.class) DictionaryDTO dictionaryDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Dictionary dictionary = BeanCopyUtils.copy(dictionaryDTO, Dictionary.class);
        dictionary.setId(dictionaryId);
        dictionary = dictionaryService.updateDictionary(dictionary);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(dictionary, fieldsView));
    }

    @ApiOperation("字典查询接口")
    @GetMapping("")
    public Result query(DictionaryQuery dictionaryQuery, FieldsView fieldsView) {
        if (dictionaryQuery.isNoPage()) {
            List<Dictionary> dictionaries = dictionaryService.query(dictionaryQuery, Dictionary.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(dictionaries, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = dictionaryService.queryPage(dictionaryQuery, Dictionary.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    @ApiOperation("字典树查询")
    @GetMapping("/tree")
    public Result tree() {
        final List<Dictionary> dictionaries = dictionaryService.findAll();
        return Result.getSuccess(new JsonTreeCreater<Dictionary>(dictionaries,
                item -> item.getId(),
                a -> StringUtils.isEmpty(a.getParentId()) ? "0" : a.getParentId(),
                a -> a.getName(),
                a -> false,
                a -> {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("parentId", a.getParentId());
                    dataMap.put("ownCode", a.getOwnCode());
                    return dataMap;
                },
                a -> false
        ).create());//以树形式返回
    }

    @ApiOperation("根据id获取字典")
    @GetMapping("/{dictionaryId}")
    public Result findById(@PathVariable("dictionaryId") String dictionaryId, FieldsView fieldsView) {
        Dictionary dictionary = dictionaryService.findById(dictionaryId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(dictionary, fieldsView));
    }

    @ApiOperation("根据id删除字典")
    @DeleteMapping("/{dictionaryId}")
    public Result deleteById(@PathVariable("dictionaryId") String dictionaryId, FieldsView fieldsView) {
        dictionaryService.deleteDictionary(dictionaryId);
        return Result.getSuccess();
    }

}
