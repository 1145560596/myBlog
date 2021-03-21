package com.atme.blog.service;

import com.atme.blog.entity.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
