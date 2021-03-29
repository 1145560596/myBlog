package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.entity.BlogComment;
import com.atme.blog.mapper.BlogCommentMapper;
import com.atme.blog.service.CommentService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    @Override
    public PageResult getCommentList(Map<String, Object> params) {
        Page<BlogComment> page = new Page<>();
        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));

        wrapper.orderByDesc("reply_create_time");
        baseMapper.selectPage(page,wrapper);

        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());
        return pageResult;
    }




}
