package com.atme.blog.service;

import com.atme.blog.controller.vo.BlogDetailVO;
import com.atme.blog.controller.vo.SimpleBlogListVO;
import com.atme.blog.entity.Blog;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
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
public interface BlogService extends IService<Blog> {

    Integer getTotalBlogs();

    PageResult getBlogList(Map<String, Object> params);

    String save1(Blog blog);

    String update1(Blog blog);

    PageResult getBlogsForIndexPage(int pageNum);

    PageResult getBlogsPageBySearch(String keyword, int page);

    List<SimpleBlogListVO> getBlogListForIndexPage(int type);

    BlogDetailVO getBlogDetail(Long blogId);

    PageResult getBlogsPageByTag(String tagName, Integer page);

    boolean deleteBlog(ArrayList<Integer> list);

    BlogDetailVO getBlogDetailBySubUrl(String subUrl);
}
