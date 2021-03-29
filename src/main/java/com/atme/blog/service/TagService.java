package com.atme.blog.service;

import com.atme.blog.entity.BlogTag;
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
public interface TagService extends IService<BlogTag> {

    Integer getTotalTags();

    PageResult paging(Map<String, Object> params);


    int create(BlogTag blogTag);

    int batchDelete(List<Integer> ids);
}
