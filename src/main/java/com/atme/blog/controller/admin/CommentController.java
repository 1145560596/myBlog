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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PostMapping("/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        if (commentService.checkDone(ids)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("审核失败");
        }
    }

    @PostMapping("/reply")
    @ResponseBody
    public Result checkDone(@RequestParam("commentId") Long commentId,
                            @RequestParam("replyBody") String replyBody) {
        if (commentId == null || commentId < 1 || StringUtils.isEmpty(replyBody)) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        if (commentService.reply(commentId, replyBody)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("回复失败");
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        Set<Integer> set = new HashSet<>();
        for (Integer id : ids) {
            set.add(id);
        }
        if (commentService.removeByIds(set)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("刪除失败");
        }
    }

}
