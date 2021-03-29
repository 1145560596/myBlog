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

        return ResultGenerator.getSuccessResult(tagService.create(blogTag));
    }

    @PostMapping("delete")
    @ResponseBody
    public Result create(@RequestBody List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return ResultGenerator.getFailResult("请选择要删除的标签");
        }
        if(tagService.batchDelete(ids) == ids.size()) {
            return ResultGenerator.getSuccessResult("删除成功");
        } else {
            return ResultGenerator.getFailResult("删除失败");
        }
    }



}

