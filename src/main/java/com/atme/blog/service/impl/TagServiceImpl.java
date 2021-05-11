package com.atme.blog.service.impl;

import com.atme.blog.controller.vo.SimpleBlogListVO;
import com.atme.blog.entity.Blog;
import com.atme.blog.entity.BlogTag;
import com.atme.blog.entity.BlogTagCount;
import com.atme.blog.entity.BlogTagRelation;
import com.atme.blog.mapper.BlogTagMapper;
import com.atme.blog.service.BlogTagRelationService;
import com.atme.blog.service.TagService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class TagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements TagService {

    @Autowired
    private BlogTagRelationService blogTagRelationService;

    @Override
    public Integer getTotalTags() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }

    @Override
    public PageResult paging(Map<String, Object> params) {

        Page<BlogTag> page = new Page<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));
        page.setSize(Long.parseLong(params.get("limit").toString()));

        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("tag_id");

        baseMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());

        return pageResult;
    }

    @Override
    public int create(BlogTag blogTag) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",blogTag.getTagName());
        BlogTag tag = baseMapper.selectOne(wrapper);
        if(Objects.isNull(tag)) {
            return baseMapper.insert(blogTag);
        }
        return 0;
    }

    @Override
    public boolean batchDelete(List<Integer> ids) {
        //已存在关联关系则不删除
        List<BlogTagRelation> blogTagRelations = blogTagRelationService.selectDistinctTagIds(ids);

        if (!CollectionUtils.isEmpty(blogTagRelations)) {
            return false;
        }
        //删除tag
        return baseMapper.deleteBatchIds(ids) == ids.size();
    }

    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        return baseMapper.getTagCount();
    }

    @Override
    public BlogTag selectByTagName(String tagName) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",tagName);
        return baseMapper.selectOne(wrapper);
    }

}
