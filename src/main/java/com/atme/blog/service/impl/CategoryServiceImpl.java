package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.mapper.BlogCategoryMapper;
import com.atme.blog.service.CategoryService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public PageResult getCategoryList(Map<String, Object> params) {
        Page<BlogCategory> page = new Page<>();
        QueryWrapper<BlogCategory> wrapper = new QueryWrapper<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));

        wrapper.orderByDesc("category_id");
        baseMapper.selectPage(page,wrapper);

        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());
        return pageResult;
    }

    /**
     * 保存
     * @param categoryName
     * @param categoryIcon
     * @return
     */
    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        QueryWrapper<BlogCategory> wrapper = new QueryWrapper<>();
        wrapper.eq("category_name",categoryName);
        BlogCategory one = baseMapper.selectOne(wrapper);

        if(Objects.isNull(one)) {
            BlogCategory category = new BlogCategory();
            category.setCategoryName(categoryName);
            category.setCategoryIcon(categoryIcon);
            return baseMapper.insert(category) > 0;
        }
        return false;
    }

    @Override
    public Integer updateCategory(Integer categoryId,String categoryName,String categoryIcon) {
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategoryId(categoryId);
        blogCategory.setCategoryName(categoryName);
        blogCategory.setCategoryIcon(categoryIcon);

        return baseMapper.updateById(blogCategory);
    }

    @Override
    public int deleteCategory(List<Integer> ids) {
        return baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<BlogCategory> selectBatchIds(List<Integer> ids) {
        return baseMapper.selectBatchIds(ids);
    }


    public BlogCategory selectByPrimaryKey(Integer blogCategoryId) {
        return baseMapper.selectById(blogCategoryId);
    }
}
