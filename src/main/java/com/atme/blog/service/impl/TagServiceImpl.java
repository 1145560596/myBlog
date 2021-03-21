package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogTag;
import com.atme.blog.mapper.BlogTagMapper;
import com.atme.blog.service.TagService;
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
public class TagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements TagService {

    @Override
    public Integer getTotalTags() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }
}
