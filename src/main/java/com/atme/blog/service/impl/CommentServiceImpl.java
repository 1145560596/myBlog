package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogComment;
import com.atme.blog.mapper.BlogCommentMapper;
import com.atme.blog.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements CommentService {

    @Override
    public Integer getTotalComments() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }
}
