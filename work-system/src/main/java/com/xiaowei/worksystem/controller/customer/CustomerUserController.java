package com.xiaowei.worksystem.controller.customer;

import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.query.rundi.query.Filter;
import com.xiaowei.core.query.rundi.query.Query;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import com.xiaowei.worksystem.entity.customer.CustomerUser;
import com.xiaowei.worksystem.service.customer.ICustomerUserService;
import com.xiaowei.worksystem.status.CommonStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "服务对象联系人")
@RestController
@RequestMapping("/api/customerUser")
public class CustomerUserController {

    @Autowired
    private ICustomerUserService customerUserService;

    @ApiOperation(value = "添加")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) CustomerUser customerUserDto, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        CustomerUser customerUser = BeanCopyUtils.copy(customerUserDto, CustomerUser.class);
        customerUser = customerUserService.save(customerUser);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customerUser, fieldsView));
    }

    @ApiOperation(value = "修改")
    @AutoErrorHandler
    @PutMapping("/{customerUserId}")
    public Result update(@RequestBody @Validated(V.Insert.class) CustomerUser customerUserDto, BindingResult bindingResult,
                         @PathVariable("customerUserId") String customerUserId, FieldsView fieldsView) throws Exception {
        CustomerUser customerUser = BeanCopyUtils.copy(customerUserDto, CustomerUser.class);
        customerUser = customerUserService.save(customerUser);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customerUser, fieldsView));
    }


    @ApiOperation("根据id获取")
    @GetMapping("/{customerUserId}")
    public Result findById(@PathVariable("customerUserId") String customerUserId, FieldsView fieldsView) {
        CustomerUser customerUser = customerUserService.findById(customerUserId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(customerUser, fieldsView));
    }

    @ApiOperation("删除")
    @DeleteMapping("/{customerUserId}")
    public Result delete(@PathVariable("customerUserId") String customerUserId, FieldsView fieldsView) {
        customerUserService.delete(customerUserId);
        return Result.getSuccess("删除成功");
    }

    @ApiOperation("查询接口")
    @GetMapping("")
    public Result query(Query query, FieldsView fieldsView) {
        query.addFilter(new Filter("status", Filter.Operator.neq, CommonStatus.DELETE.getStatus()));
        if (query.isNoPage()) {
            List<CustomerUser> customerUsers = customerUserService.query(query, CustomerUser.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(customerUsers, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = customerUserService.queryPage(query, CustomerUser.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

}
