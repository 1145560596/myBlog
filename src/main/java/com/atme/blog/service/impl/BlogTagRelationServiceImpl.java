package com.atme.blog.service.impl;


import com.atme.blog.entity.Blog;
import com.atme.blog.entity.BlogTagRelation;
import com.atme.blog.mapper.BlogTagRelationMapper;
import com.atme.blog.service.BlogTagRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-10-21-19:40
 */
@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRelation> implements BlogTagRelationService {

    @Override
    public List<Long> selectBlogIdsByTagId(Integer tagId) {
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id",tagId);
        List<BlogTagRelation> blogTagRelations = baseMapper.selectList(wrapper);
        return blogTagRelations.stream().map(BlogTagRelation::getBlogId).collect(Collectors.toList());
    }

    @Override
    public List<BlogTagRelation> selectDistinctTagIds(List<Integer> ids) {
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.in("tag_id",ids);
        List<BlogTagRelation> blogTagRelations = baseMapper.selectList(wrapper);
        return blogTagRelations;
    }
}
