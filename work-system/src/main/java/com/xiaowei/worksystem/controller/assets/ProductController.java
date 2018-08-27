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
import com.xiaowei.worksystem.entity.assets.Product;
import com.xiaowei.worksystem.service.assets.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "产品目录")
@Log4j2
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) Product productDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Product product = BeanCopyUtils.copy(productDto, Product.class);
        product = productService.save(product);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(product, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{productId}")
    public Result update(@RequestBody @Validated(V.Insert.class) Product productDto, BindingResult bindingResult,
                         @PathVariable("productId") String productId, FieldsView fieldsView) throws Exception {
        Product product = BeanCopyUtils.copy(productDto, Product.class);
        product = productService.save(product);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(product, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{productId}")
    public Result findById(@PathVariable("productId") String productId, FieldsView fieldsView) {
        Product product = productService.findById(productId);
        return Result.getSuccess(ObjectToMapUtils.anyToHandleField(product, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{productId}")
    public Result delete(@PathVariable("productId") String productId, FieldsView fieldsView) {
        productService.delete(productId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<Product> products = productService.query(query, Product.class);
            return Result.getSuccess(ObjectToMapUtils.anyToHandleField(products, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = productService.queryPage(query, Product.class);
            pageResult.setRows(ObjectToMapUtils.anyToHandleField(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
