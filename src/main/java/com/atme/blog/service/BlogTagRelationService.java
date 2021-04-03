package com.atme.blog.service;

import com.atme.blog.entity.Blog;
import com.atme.blog.entity.BlogTagRelation;
import com.atme.blog.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-10-21-19:39
 */
public interface BlogTagRelationService extends IService<BlogTagRelation> {
    List<Long> selectBlogIdsByTagId(Integer tagId);

    List<BlogTagRelation> selectDistinctTagIds(List<Integer> ids);
}
