package com.xiaowei.account.service.impl;

import com.xiaowei.account.entity.Post;
import com.xiaowei.account.repository.PostRepository;
import com.xiaowei.account.service.IPostService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 公司服务
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<Post> implements IPostService {

    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl(@Qualifier("postRepository")BaseRepository repository) {
        super(repository);
    }

}
