package com.atme.blog.controller.blog;

import com.atme.blog.service.*;
import com.atme.blog.utils.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 顾文杰
 * @create 2021-03-27-10:35
 */
@Controller
public class MyBlogController {

    public static String theme = "amaze";

    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private LinkService linkService;
    @Resource
    private CommentService commentService;
    @Resource
    private ConfigService configService;
    @Resource
    private CategoryService categoryService;


    /**
     * 首页
     *
     * @return
     */
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
//        request.setAttribute("blogPageResult", blogPageResult);
//        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
//        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
//        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
//        request.setAttribute("pageName", "首页");
//        request.setAttribute("configurations", configService.getAllConfigs());
//        return "blog/" + theme + "/index";
        return null;
    }


}