package com.xiaowei.account.service;


import com.xiaowei.account.entity.Post;
import com.xiaowei.core.basic.service.IBaseService;

public interface IPostService extends IBaseService<Post> {

    Post savePost(Post post);

    Post updatePost(Post post);

    Post updateStatus(Post post);
}
