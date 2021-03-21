package com.atme.blog.service;

import com.atme.blog.entity.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface AdminUserService extends IService<AdminUser> {

    AdminUser login(String userName,String password);

    int updateName(String loginUserName, String nickName, Integer loginUserId);

    boolean updatePassword(String originalPassword,String newPassword, Integer loginUserId);
}
