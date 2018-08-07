package com.xiaowei.accountweb.rest;

import com.xiaowei.account.consts.SuperUser;
import com.xiaowei.account.entity.Post;
import com.xiaowei.account.query.PostQuery;
import com.xiaowei.account.service.IPostService;
import com.xiaowei.account.utils.AccountUtils;
import com.xiaowei.accountcommon.LoginUserBean;
import com.xiaowei.accountcommon.LoginUserUtils;
import com.xiaowei.accountweb.dto.PostDTO;
import com.xiaowei.core.bean.BeanCopyUtils;
import com.xiaowei.core.result.FieldsView;
import com.xiaowei.core.result.PageResult;
import com.xiaowei.core.result.Result;
import com.xiaowei.core.utils.ObjectToMapUtils;
import com.xiaowei.core.validate.AutoErrorHandler;
import com.xiaowei.core.validate.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位管理
 */
@Api(tags = "岗位接口")
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private IPostService postService;



    @RequiresPermissions("account:post:add")
    @ApiOperation(value = "添加岗位")
    @AutoErrorHandler
    @PostMapping("")
    public Result insert(@RequestBody @Validated(V.Insert.class) PostDTO postDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Post post = BeanCopyUtils.copy(postDTO, Post.class);
        post = postService.savePost(post);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(post, fieldsView));
    }

    @RequiresPermissions("account:post:update")
    @ApiOperation(value = "修改岗位")
    @AutoErrorHandler
    @PutMapping("/{postId}")
    public Result update(@PathVariable("postId") String postId, @RequestBody @Validated(V.Update.class) PostDTO postDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Post post = BeanCopyUtils.copy(postDTO, Post.class);
        post.setId(postId);
        post = postService.updatePost(post);
        AccountUtils.loadUser();
        return Result.getSuccess(ObjectToMapUtils.objectToMap(post, fieldsView));
    }

    @RequiresPermissions("account:post:status")
    @ApiOperation(value = "启用/禁用岗位")
    @AutoErrorHandler
    @PutMapping("/{postId}/status")
    public Result updateStatus(@PathVariable("postId") String postId, @RequestBody @Validated(PostDTO.UpdateStatus.class) PostDTO postDTO, BindingResult bindingResult, FieldsView fieldsView) throws Exception {
        Post post = BeanCopyUtils.copy(postDTO, Post.class);
        post.setId(postId);
        post = postService.updateStatus(post);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(post, fieldsView));
    }


    @RequiresPermissions("account:post:query")
    @ApiOperation("岗位查询接口")
    @GetMapping("")
    public Result query(PostQuery postQuery, FieldsView fieldsView) {
        //查询公司设置默认条件
        setDefaultCondition(postQuery);
        if (postQuery.isNoPage()) {
            List<Post> posts = postService.query(postQuery, Post.class);
            return Result.getSuccess(ObjectToMapUtils.listToMap(posts, fieldsView));//以list形式返回,没有层级
        } else {
            PageResult pageResult = postService.queryPage(postQuery, Post.class);
            pageResult.setRows(ObjectToMapUtils.listToMap(pageResult.getRows(), fieldsView));
            return Result.getSuccess(pageResult);//以分页列表形式返回
        }
    }

    private void setDefaultCondition(PostQuery postQuery) {
        LoginUserBean loginUser = LoginUserUtils.getLoginUser();
        if(!SuperUser.ADMINISTRATOR_NAME.equals(loginUser.getLoginName())){
            postQuery.setCompanyId(loginUser.getCompanyBean().getId());
        }
    }

    @RequiresPermissions("account:post:get")
    @ApiOperation("根据id获取岗位")
    @GetMapping("/{postId}")
    public Result findById(@PathVariable("postId") String postId, FieldsView fieldsView) {
        Post post = postService.findById(postId);
        return Result.getSuccess(ObjectToMapUtils.objectToMap(post, fieldsView));
    }

}
