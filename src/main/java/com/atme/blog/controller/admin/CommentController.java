package com.atme.blog.controller.admin;

import com.atme.blog.service.CategoryService;
import com.atme.blog.service.CommentService;
import com.atme.blog.utils.Result;
import com.atme.blog.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-10-18-16:23
 */
@Controller
@RequestMapping("/admin/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping("")
    public String categories(HttpServletRequest request) {
        request.setAttribute("path","comments");
        return "admin/comment";
    }

    /**
     * 分类列表
     */
    @GetMapping("list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        return ResultGenerator.getSuccessResult(commentService.getCommentList(params));
    }

    /**
     * 新增
     */
//    @PostMapping("save")
//    public Result save(@RequestParam )


}
