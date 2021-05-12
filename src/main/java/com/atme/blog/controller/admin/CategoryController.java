package com.atme.blog.controller.admin;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.service.CategoryService;
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
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.getFailResult("参数异常！");
        }
        return ResultGenerator.getSuccessResult(categoryService.getCategoryList(params));
    }

    /**
     * 分类添加
     */
    @PostMapping("save")
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {

        if (StringUtils.isEmpty(categoryName)) {
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

    @PostMapping("update")
    @ResponseBody
    public Result edit(@RequestParam("categoryId") Integer categoryId,
                       @RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon) {
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.getFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.getFailResult("请选择分类图标！");
        }
        if (categoryService.updateCategory(categoryId, categoryName, categoryIcon) > 0) {
            return ResultGenerator.getSuccessResult();
        } else {
            return ResultGenerator.getFailResult("分类名称重复");
        }
    }

    @PostMapping("delete")
    @ResponseBody
    public Result edit(@RequestBody List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResultGenerator.getFailResult("请选择要删除的分类！");
        }
        List<BlogCategory> blogCategories = categoryService.deleteCategory(ids);
        if (CollectionUtils.isEmpty(blogCategories)) {
            return ResultGenerator.getSuccessResult();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            int size = blogCategories.size();
            AtomicInteger num = new AtomicInteger();
            blogCategories.stream().forEach(blogCategory -> {
                stringBuilder.append(blogCategory.getCategoryName());
                num.getAndIncrement();
                if(num.get() != size) {
                    stringBuilder.append("、");
                }
            });
            return ResultGenerator.getFailResult("分类：“"+ stringBuilder + "” 被文章所引用，请先删除文章");
        }
    }





}
