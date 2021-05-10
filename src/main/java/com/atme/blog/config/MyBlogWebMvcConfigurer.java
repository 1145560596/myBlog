package com.atme.blog.config;

import com.atme.blog.interceptor.MyAdminInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器要拦哪些请求
 *
 * @author shkstart
 * @create 2020-10-19-8:48
 */
@Configuration
public class MyBlogWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private MyAdminInterceptor myAdminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myAdminInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
    }

}
