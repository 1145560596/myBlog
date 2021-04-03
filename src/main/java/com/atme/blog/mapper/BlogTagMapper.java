package com.atme.blog.mapper;

import com.atme.blog.entity.BlogTag;
import com.atme.blog.entity.BlogTagCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    List<BlogTagCount> getTagCount();
}
