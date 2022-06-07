package com.atme.blog.service.impl;

import com.atme.blog.entity.AdminUser;
import com.atme.blog.mapper.AdminUserMapper;
import com.atme.blog.service.AdminUserService;
import com.atme.blog.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Override
    public AdminUser login(String userName, String password) {
        String md5Encode = MD5Util.MD5Encode(password, "utf-8");
        //sadsadaw
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("login_user_name",userName);
        wrapper.eq("login_password",md5Encode);
        wrapper.eq("locked",0);
        AdminUser adminUser = baseMapper.selectOne(wrapper);

        return adminUser;
    }

    @Override
    public int updateName(String loginUserName, String nickName, Integer loginUserId) {
        AdminUser adminUser = new AdminUser();
        adminUser.setAdminUserId(loginUserId);
        adminUser.setLoginUserName(loginUserName);
        adminUser.setNickName(nickName);
        return baseMapper.updateById(adminUser);
    }

    @Override
    public boolean updatePassword(String originalPassword,String newPassword, Integer loginUserId) {
        AdminUser user = baseMapper.selectById(loginUserId);
        //用户是否存在
        if(user != null) {
            String password = user.getLoginPassword();
            String originalPasswordMD5 = MD5Util.MD5Encode(originalPassword, "utf-8");
            //原密码与新密码是否相同
            if (password.equals(originalPasswordMD5)) {
                String newPasswordMD5 = MD5Util.MD5Encode(newPassword, "utf-8");
                AdminUser adminUser = new AdminUser();
                adminUser.setAdminUserId(loginUserId);
                adminUser.setLoginPassword(newPasswordMD5);
                return baseMapper.updateById(adminUser) > 0;
            }
            return false;
        }
        return false;
    }

}










