package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.mapper.BlogCategoryMapper;
import com.atme.blog.service.CategoryService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements CategoryService {

    @Override
    public Integer getTotalCategories() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }

    @Override
    public PageResult getCategoryList() {
        Page<BlogCategory> page = new Page<>();
        QueryWrapper<BlogCategory> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("category_id");
        baseMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getTotal(),page.getCurrent(),page.getRecords());
        return pageResult;
    }


}
