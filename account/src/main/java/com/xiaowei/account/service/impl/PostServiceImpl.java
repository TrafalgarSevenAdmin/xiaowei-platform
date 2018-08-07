package com.xiaowei.account.service.impl;

import com.xiaowei.account.consts.PostStatus;
import com.xiaowei.account.entity.Company;
import com.xiaowei.account.entity.Post;
import com.xiaowei.account.repository.CompanyRepository;
import com.xiaowei.account.repository.PostRepository;
import com.xiaowei.account.service.IPostService;
import com.xiaowei.core.basic.repository.BaseRepository;
import com.xiaowei.core.basic.service.impl.BaseServiceImpl;
import com.xiaowei.core.utils.EmptyUtils;
import com.xiaowei.core.validate.JudgeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 公司服务
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<Post> implements IPostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CompanyRepository companyRepository;

    public PostServiceImpl(@Qualifier("postRepository")BaseRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Post savePost(Post post) {
        //判定参数是否合规
        judgeAttribute(post, JudgeType.INSERT);
        postRepository.save(post);
        return post;
    }

    private void judgeAttribute(Post post, JudgeType judgeType) {
        if(judgeType.equals(JudgeType.INSERT)){//保存
            post.setId(null);
            //设置code
            post.setCode(UUID.randomUUID().toString());
            //设置创建时间
            post.setCreatedTime(new Date());
            //默认状态正常
            post.setStatus(PostStatus.NORMAL.getStatus());
        }else if(judgeType.equals(JudgeType.UPDATE)) {//修改
            String postId = post.getId();
            EmptyUtils.assertString(postId,"没有传入对象id");
            Optional<Post> optional = postRepository.findById(postId);
            EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
            Post one = optional.get();

            //设置不允许在此处修改的属性
            post.setStatus(one.getStatus());
        }
        //验证所属公司
        EmptyUtils.assertObject(post.getCompany(), "所属公司为空");
        EmptyUtils.assertString(post.getCompany().getId(), "所属公司id为空");
        Optional<Company> company = companyRepository.findById(post.getCompany().getId());
        EmptyUtils.assertOptional(company,"没有查询到所属公司");
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        //判定参数是否合规
        judgeAttribute(post, JudgeType.UPDATE);
        postRepository.save(post);
        return post;
    }

    @Override
    @Transactional
    public Post updateStatus(Post post) {
        String postId = post.getId();
        EmptyUtils.assertString(postId,"没有传入对象id");
        Optional<Post> optional = postRepository.findById(postId);
        EmptyUtils.assertOptional(optional,"没有查询到需要修改的对象");
        Post one = optional.get();
        EmptyUtils.assertObject(one,"没有查询到需要删除的对象");
        one.setStatus(post.getStatus());
        postRepository.save(one);
        return one;
    }
}
