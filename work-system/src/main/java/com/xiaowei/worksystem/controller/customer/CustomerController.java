package com.xiaowei.worksystem.controller.customer;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.customer.Customer;
import com.xiaowei.worksystem.service.customer.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "服务对象")
@Slf4j
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @ApiOperation("获取所有区县")
    @GetMapping("/countys")
    @RequiresPermissions("order:customer:info:countys")
    public Result getCountys() {
        List<String> countys = customerService.getCountys();
        return Result.getSuccess(countys);
    }

    @ApiOperation("获取区县下的服务对象")
    @GetMapping("/customerOfCountys")
    @RequiresPermissions("order:customer:info:byCounty")
    public Result getCustomerOfCountys(String county) {
        List<Customer> customers = customerService.getCustomerByCountys(county);
        return Result.getSuccess(customers);
    }

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    @RequiresPermissions("order:customer:add")
    public Result insert(@RequestBody @Validated(V.Insert.class) Customer customerDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Customer customer = BeanCopyUtils.copy(customerDto, Customer.class);
        customer = customerService.save(customer);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customer, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{customerId}")
    @RequiresPermissions("order:customer:update")
    public Result update(@RequestBody @Validated(V.Insert.class) Customer customerDto, BindingResult bindingResult,
                         @PathVariable("customerId") String customerId, FieldsView fieldsView) throws Exception {
        Customer customer = BeanCopyUtils.copy(customerDto, Customer.class);
        customer = customerService.save(customer);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customer, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{customerId}")
    @RequiresPermissions("order:customer:get")
    public Result findById(@PathVariable("customerId") String customerId, FieldsView fieldsView) {
        Customer customer = customerService.findById(customerId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customer, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{customerId}")
    @RequiresPermissions("order:customer:delete")
    public Result delete(@PathVariable("customerId") String customerId, FieldsView fieldsView) {
        customerService.delete(customerId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    @RequiresPermissions("order:customer:query")
    public Result query(Query query, FieldsView fieldsView) {
        if (query.isNoPage()) {
            List<Customer> customers = customerService.query(query, Customer.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(customers, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = customerService.queryPage(query, Customer.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
