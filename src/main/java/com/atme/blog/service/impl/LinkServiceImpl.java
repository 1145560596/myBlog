package com.atme.blog.service.impl;

import com.atme.blog.entity.BlogCategory;
import com.atme.blog.entity.Link;
import com.atme.blog.mapper.LinkMapper;
import com.atme.blog.service.LinkService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

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

    @Override
    public PageResult getLinkList(Map<String, Object> params) {
        Page<Link> page = new Page<>();
        QueryWrapper<Link> wrapper = new QueryWrapper<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));

        wrapper.orderByDesc("create_time");
        baseMapper.selectPage(page,wrapper);

        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());
        return pageResult;
    }

    @Override
    public Boolean saveLink(Link link) {
        return baseMapper.insert(link) > 0;
    }

    @Override
    public Link selectById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer updateLink(Link tempLink) {
        return baseMapper.updateById(tempLink);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer id : ids) {
            list.add(id);
        }
        return baseMapper.deleteBatchIds(list);
    }


}
