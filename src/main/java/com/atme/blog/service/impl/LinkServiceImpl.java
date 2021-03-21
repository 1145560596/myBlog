package com.atme.blog.service.impl;

import com.atme.blog.entity.Link;
import com.atme.blog.mapper.LinkMapper;
import com.atme.blog.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public Integer getTotalLinks() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }
}
