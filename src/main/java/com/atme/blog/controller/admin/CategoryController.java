package com.atme.blog.controller.admin;

import com.atme.blog.service.CategoryService;
import com.atme.blog.utils.Result;
import com.atme.blog.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 顾文杰
 * @create 2020-10-18-16:22
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public String categories(HttpServletRequest request) {
        request.setAttribute("path","categories");
        return "admin/category";
    }

    /**
     * 分类列表
     */
    @GetMapping("list")
    @ResponseBody
    public Result list() {
        return ResultGenerator.getSuccessResult(categoryService.getCategoryList());
    }

    /**
     * 分类添加
     */
    @PostMapping("save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {

        if (org.springframework.util.StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.getFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.getFailResult("请选择分类图标！");
        }
        if (categoryService.saveCategory(categoryName, categoryIcon)) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("分类名称重复");
        }

    }



}
