package com.atme.blog.service;

import com.atme.blog.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface LinkService extends IService<Link> {

    Integer getTotalLinks();
}
