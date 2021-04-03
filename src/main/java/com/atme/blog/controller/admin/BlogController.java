package com.atme.blog.controller.admin;


import com.atme.blog.entity.Blog;
import com.atme.blog.service.BlogService;
import com.atme.blog.service.CategoryService;
import com.atme.blog.service.impl.BlogServiceImpl;
import com.atme.blog.service.impl.CategoryServiceImpl;
import com.atme.blog.utils.Result;
import com.atme.blog.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Controller
@RequestMapping("/admin/blogs")
public class BlogController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    BlogService blogService;


    //博客管理
    @GetMapping("")
    public String list(HttpServletRequest request) {
        request.setAttribute("path","blogs");
        return "admin/blog";
    }

    //博客列表
    @GetMapping("list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params) {
        if(StringUtils.isEmpty((String) params.get("page")) || StringUtils.isEmpty((String) params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常");
        }
        return ResultGenerator.getSuccessResult(blogService.getBlogList(params));
    }

    //进入发布博客页面
    @GetMapping("edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path","edit");
        //查找类别
        request.setAttribute("categories",categoryService.list(null));
        return "admin/edit";
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Integer blogStatus,
                       @RequestParam("enableComment") Integer enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.getFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.getFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.getFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.getFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.getFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.getFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.getFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.getFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);

        if(StringUtils.equals(blogService.save1(blog),"success")) {
            return ResultGenerator.getSuccessResult("添加成功");
        } else {
            return ResultGenerator.getFailResult("添加失败");
        }
    }

    //进去修改博客页面
    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id,HttpServletRequest request) {
        Blog blog = blogService.getById(id);
        if(blog == null) {
            return "error/error_400.html";
        }
        request.setAttribute("path","edit");
        request.setAttribute("blog",blog);
        request.setAttribute("categories",categoryService.list(null));
        return "admin/edit";
    }

    @PostMapping("update")
    @ResponseBody
    public Result update(@RequestParam("blogId") long blogId,
                       @RequestParam("blogTitle") String blogTitle,
                       @RequestParam(name = "blogSubUrl", required = false) String blogSubUrl,
                       @RequestParam("blogCategoryId") Integer blogCategoryId,
                       @RequestParam("blogTags") String blogTags,
                       @RequestParam("blogContent") String blogContent,
                       @RequestParam("blogCoverImage") String blogCoverImage,
                       @RequestParam("blogStatus") Integer blogStatus,
                       @RequestParam("enableComment") Integer enableComment) {
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.getFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.getFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.getFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.getFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.getFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.getFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.getFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.getFailResult("封面图不能为空");
        }
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);

        if(StringUtils.equals(blogService.update1(blog),"success")) {
            return ResultGenerator.getSuccessResult("修改成功");
        } else {
            return ResultGenerator.getFailResult("修改失败");
        }
    }

    @PostMapping("delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer id : ids) {
            list.add(id);
        }
        if(ids == null || ids.length < 1) {
            return ResultGenerator.getFailResult("参数不能为空");
        }
        if(blogService.deleteBlog(list) == true) {
            return ResultGenerator.getSuccessResult("删除成功");
        }
        return ResultGenerator.getFailResult("删除失败");
    }




}


