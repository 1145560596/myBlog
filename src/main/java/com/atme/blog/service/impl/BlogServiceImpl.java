package com.atme.blog.service.impl;

import com.atme.blog.entity.*;
import com.atme.blog.mapper.BlogMapper;
import com.atme.blog.mapper.BlogTagMapper;
import com.atme.blog.mapper.BlogTagRelationMapper;
import com.atme.blog.service.BlogService;
import com.atme.blog.service.CategoryService;
import com.atme.blog.service.CommentService;
import com.atme.blog.service.TagService;
import com.atme.blog.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private TagServiceImpl tagService;

    @Autowired
    private BlogTagRelationServiceImpl blogTagRelationService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;


    @Override
    public Integer getTotalBlogs() {
        Integer count = baseMapper.selectCount(null);
        return count;
    }

    @Override
    public PageResult getBlogList(Map<String, Object> params) {
        String keyword = (String) params.get("keyword");
        Page<Blog> page = new Page<>();
        page.setCurrent(Integer.valueOf(params.get("page").toString()));

        //搜索功能
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like("blog_title","%"+keyword+"%");
            wrapper.or(new Function<QueryWrapper<Blog>, QueryWrapper<Blog>>() {
                @Override
                public QueryWrapper<Blog> apply(QueryWrapper<Blog> blogQueryWrapper) {
                    QueryWrapper<Blog> wrapper1 = new QueryWrapper<>();
                    wrapper1.like("blog_category_name","%"+keyword+"%");
                    return wrapper1;
                }
            });
        }

        wrapper.orderByDesc("blog_id");
        baseMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),page.getRecords());
        return pageResult;
    }

    @Override
    @Transactional
    public String save1(Blog blog) {
        BlogCategory category = categoryService.getById(blog.getBlogCategoryId());
        blog.setBlogCategoryName(category.getCategoryName());

        String blogTag = blog.getBlogTags();
        String[] blogTags = blogTag.split(",");
        if (blogTags.length > 6) {
            return "标签数不能超过6";
        }

        if(baseMapper.insert(blog) > 0) {
            //更新某分类的数量
            category.setCategoryRank(category.getCategoryRank() + 1);
            categoryService.updateById(category);

            ArrayList<BlogTag> newBlogTags = new ArrayList<>();
            ArrayList<BlogTag> allBlogTags = new ArrayList<>();

            for (String tagName : blogTags) {
                QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
                wrapper.eq("tag_name",tagName);
                BlogTag one = tagService.getOne(wrapper);
                if(one == null) {
                    BlogTag blogTag1 = new BlogTag();
                    blogTag1.setTagName(tagName);
                    newBlogTags.add(blogTag1);
                } else {
                    allBlogTags.add(one);
                }
            }
            //保存新的标签
            if(!CollectionUtils.isEmpty(newBlogTags)) {
                tagService.saveBatch(newBlogTags);
            }
            allBlogTags.addAll(newBlogTags);

            //标签与博客建立关系
            ArrayList<BlogTagRelation> blogTagRelationList = new ArrayList<>();
            for (BlogTag tag : allBlogTags) {
                BlogTagRelation tagRelation = new BlogTagRelation();
                tagRelation.setBlogId(blog.getBlogId());
                tagRelation.setTagId(tag.getTagId());
                blogTagRelationList.add(tagRelation);
            }
            blogTagRelationService.saveBatch(blogTagRelationList);
            return "success";
        }
        return "保存失败";
    }

    @Override
    @Transactional
    public String update1(Blog blog) {
        //从数据库中获取原来的博客
        Blog blogFromDB = blogService.getById(blog.getBlogId());
        if(blogFromDB == null) {
            return "数据不存在";
        }

        String blogTag = blog.getBlogTags();
        String[] blogTags = blogTag.split(",");
        if (blogTags.length > 6) {
            return "标签数不能超过6";
        }

        //1.判断博客的类别是否发生改变
        BlogCategory newCategory = categoryService.getById(blog.getBlogCategoryId());
        if(blog.getBlogCategoryId() != blogFromDB.getBlogCategoryId()) {
            //发生改变
            BlogCategory oldCategory = categoryService.getById(blogFromDB.getBlogCategoryId());
            blog.setBlogCategoryName(newCategory.getCategoryName());
            newCategory.setCategoryRank(newCategory.getCategoryRank() + 1);
            oldCategory.setCategoryRank(oldCategory.getCategoryRank() - 1);
            categoryService.updateById(newCategory);
            categoryService.updateById(oldCategory);
        }

        //2.判断更新是否成功
        if(baseMapper.updateById(blog) > 0) {
            ArrayList<BlogTag> newBlogTags = new ArrayList<>();
            ArrayList<BlogTag> allBlogTags = new ArrayList<>();
            for (String tagName : blogTags) {
                QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
                wrapper.eq("tag_name",tagName);
                BlogTag one = tagService.getOne(wrapper);
                if(one == null) {
                    BlogTag blogTag1 = new BlogTag();
                    blogTag1.setTagName(tagName);
                    newBlogTags.add(blogTag1);
                } else {
                    allBlogTags.add(one);
                }
            }
            //保存新的标签
            if(!CollectionUtils.isEmpty(newBlogTags)) {
                tagService.saveBatch(newBlogTags);
            }
            allBlogTags.addAll(newBlogTags);

            //标签与博客建立关系
            ArrayList<BlogTagRelation> blogTagRelationList = new ArrayList<>();
            for (BlogTag tag : allBlogTags) {
                BlogTagRelation tagRelation = new BlogTagRelation();
                tagRelation.setBlogId(blog.getBlogId());
                tagRelation.setTagId(tag.getTagId());
                blogTagRelationList.add(tagRelation);
            }
            //先删除原有关系数据,再保存新的关系数据
            QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
            wrapper.eq("blog_id",blog.getBlogId());
            blogTagRelationService.remove(wrapper);
            blogTagRelationService.saveBatch(blogTagRelationList);
            return "success";
        }
        return "更新失败";
    }

    @Override
    public PageResult getBlogsForIndexPage(int pageNum) {

        return null;
    }


}
