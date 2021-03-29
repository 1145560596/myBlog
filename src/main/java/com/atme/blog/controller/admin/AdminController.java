package com.atme.blog.controller.admin;


import com.atme.blog.entity.AdminUser;
import com.atme.blog.service.*;
import com.atme.blog.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminUserService adminUserService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;


    //进入登录页面
    @GetMapping("login")
    public String login() {
        return "/admin/login";
    }


    //login界面按下登录
    @PostMapping("login")
    public String login(@RequestParam String userName,
                        @RequestParam String password,
                        @RequestParam String verifyCode,
                        HttpSession session) {
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "/admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "/admin/login";
        }
        String kpatcha = (String) session.getAttribute("verifyCode");
        if (!StringUtils.equals(verifyCode, kpatcha)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "/admin/login";
        }
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            session.setAttribute("userName", userName);
            session.setMaxInactiveInterval(3600 * 3);

            return "redirect:/admin/index";
        } else {
            session.setAttribute("errorMsg", "登录失败");
            return "admin/login";
        }
    }

    //后台页面的dashboard数据显示
    @GetMapping({"index"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.getTotalCategories());
        request.setAttribute("blogCount", blogService.getTotalBlogs());
        request.setAttribute("linkCount", linkService.getTotalLinks());
        request.setAttribute("tagCount", tagService.getTotalTags());
        request.setAttribute("commentCount", commentService.getTotalComments());

        return "admin/index";
    }

    //修改密码
    @GetMapping("profile")
    public String profile(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("userName");
        String nickName = (String) session.getAttribute("loginUser");

        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", userName);
        request.setAttribute("nickName", nickName);
        return "admin/profile";
    }

    //修改登录名称和昵称
    @PostMapping("profile/name")
    @ResponseBody
    public String updateName(@PathParam("nickName") String nickName, @PathParam("loginUserName") String loginUserName,
                             HttpServletRequest request) {
        if (StringUtils.isEmpty(nickName) || StringUtils.isEmpty(loginUserName)) {
            return "登录名称或昵称不能为空";
        }
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserName, nickName, loginUserId) > 0) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    //修改登录密码
    @PostMapping("profile/password")
    @ResponseBody
    public String updatePassword(@PathParam("originalPassword") String originalPassword, @PathParam("newPassword") String newPassword,
                                 HttpServletRequest request) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        if(originalPassword.equals(newPassword)) {
            return "新密码与原密码不能相同";
        }
        Integer loginUserId = (Integer) request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(originalPassword,newPassword,loginUserId)) {
            request.getSession().removeAttribute("errorMsg");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("userName");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "admin/login";
    }



}