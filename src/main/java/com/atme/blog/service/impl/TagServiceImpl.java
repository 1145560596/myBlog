package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogTag;
import com.atme.blog.mapper.BlogTagMapper;
import com.atme.blog.service.TagService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.NumberUtils;

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

    @Override
    public Integer getTotalTags() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }

    @Override
    public PageResult paging(Map<String, Object> params) {

        Page<BlogTag> page = new Page<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("tag_id");

        baseMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());

        return pageResult;
    }

    @Override
    public int create(BlogTag blogTag) {
        return baseMapper.insert(blogTag);
    }

    @Override
    public int batchDelete(List<Integer> ids) {
        return baseMapper.deleteBatchIds(ids);
    }

}
