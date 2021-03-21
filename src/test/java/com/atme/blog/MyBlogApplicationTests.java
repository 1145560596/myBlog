package com.atme.blog;

import com.atme.blog.entity.Blog;
import com.atme.blog.entity.BlogCategory;
import com.atme.blog.service.BlogService;
import com.atme.blog.utils.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBlogApplicationTests {

    @Autowired
    private BlogService blogService;

    @Test
    public void contextLoads() {
        String s = MD5Util.MD5Encode("c4ca4238a0b923820dcc509a6f75849b", "utf-8");
        System.out.println(s);


    }



}
