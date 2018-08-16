package com.xiaowei.worksystem.controller.assets;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.assets.ProBrand;
import com.xiaowei.worksystem.service.assets.IProBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "产品品牌表")
@Slf4j
@RestController
@RequestMapping("/api/assets/proBrand")
public class ProBrandController {

    @Autowired
    private IProBrandService proBrandService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) ProBrand proBrandDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        ProBrand proBrand = BeanCopyUtils.copy(proBrandDto, ProBrand.class);
        proBrand = proBrandService.save(proBrand);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proBrand, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{proBrandId}")
    public Result update(@RequestBody @Validated(V.Insert.class) ProBrand proBrandDto, BindingResult bindingResult,
                         @PathVariable("proBrandId") String proBrandId, FieldsView fieldsView) throws Exception {
        ProBrand proBrand = BeanCopyUtils.copy(proBrandDto, ProBrand.class);
        proBrand = proBrandService.save(proBrand);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proBrand, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{proBrandId}")
    public Result findById(@PathVariable("proBrandId") String proBrandId, FieldsView fieldsView) {
        ProBrand proBrand = proBrandService.findById(proBrandId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(proBrand, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{proBrandId}")
    public Result delete(@PathVariable("proBrandId") String proBrandId, FieldsView fieldsView) {
        proBrandService.delete(proBrandId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<ProBrand> proBrands = proBrandService.query(query, ProBrand.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(proBrands, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = proBrandService.queryPage(query, ProBrand.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
