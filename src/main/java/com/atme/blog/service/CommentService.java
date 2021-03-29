package com.atme.blog.service;

import com.atme.blog.entity.BlogComment;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface CommentService extends IService<BlogComment> {

    Integer getTotalComments();

    PageResult getCommentList(Map<String, Object> params);
}
