package com.atme.blog.service;

import com.atme.blog.entity.Link;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface LinkService extends IService<Link> {

    Integer getTotalLinks();

    PageResult getLinkList(Map<String, Object> params);

    Boolean saveLink(Link link);

    Link selectById(Integer id);

    Integer updateLink(Link tempLink);

    int deleteBatch(Integer[] ids);

    Map<Integer, List<Link>> getLinksForLinkPage();
}
