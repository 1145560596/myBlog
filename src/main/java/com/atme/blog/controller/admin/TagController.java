package com.atme.blog.controller.admin;


import com.atme.blog.entity.BlogTag;
import com.atme.blog.service.TagService;
import com.atme.blog.utils.Result;
import com.atme.blog.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("")
    public String tagPage(HttpServletRequest request) {
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @GetMapping("list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        return ResultGenerator.getSuccessResult(tagService.paging(params));
    }

    @PostMapping("save")
    @ResponseBody
    public Result create(@RequestParam("tagName") String tagName) {
        if(StringUtils.isEmpty(tagName)) {
            return ResultGenerator.getFailResult("请输入标签名称");
        }
        BlogTag blogTag = new BlogTag();
        blogTag.setTagName(tagName);

        if(tagService.create(blogTag) == 1) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("已经有该名称的标签,创建失败");
        }
    }

    @PostMapping("delete")
    @ResponseBody
    public Result delete(@RequestBody List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return ResultGenerator.getFailResult("请选择要删除的标签");
        }
        List<BlogTag> blogTags = tagService.batchDelete(ids);
        if(CollectionUtils.isEmpty(blogTags)) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            int size = blogTags.size();
            AtomicInteger num = new AtomicInteger();
            blogTags.stream().forEach(blogTag -> {
                stringBuilder.append(blogTag.getTagName());
                num.getAndIncrement();
                if(num.get() != size) {
                    stringBuilder.append("、");
                }
            });
            return ResultGenerator.getFailResult("标签：“"+ stringBuilder + "” 被文章所引用，请先删除文章");
        }
    }



}

