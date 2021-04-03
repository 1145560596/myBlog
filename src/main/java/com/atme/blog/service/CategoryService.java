package com.atme.blog.service;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface CategoryService extends IService<BlogCategory> {

    Integer getTotalCategories();

    PageResult getCategoryList(Map<String, Object> params);

    Boolean saveCategory(String categoryName,String categoryIcon);

    Integer updateCategory(Integer categoryId,String categoryName,String categoryIcon);

    int deleteCategory(List<Integer> ids);

    List<BlogCategory> selectBatchIds(List<Integer> ids);

    BlogCategory selectByPrimaryKey(Integer blogCategoryId);
}
