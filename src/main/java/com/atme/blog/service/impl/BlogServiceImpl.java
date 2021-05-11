package com.atme.blog.service.impl;

import com.atme.blog.controller.vo.BlogDetailVO;
import com.atme.blog.controller.vo.BlogListVO;
import com.atme.blog.controller.vo.SimpleBlogListVO;
import com.atme.blog.entity.*;
import com.atme.blog.mapper.BlogMapper;
import com.atme.blog.mapper.BlogTagMapper;
import com.atme.blog.mapper.BlogTagRelationMapper;
import com.atme.blog.service.*;
import com.atme.blog.utils.MarkDownUtil;
import com.atme.blog.utils.PageResult;
import com.atme.blog.utils.PatternUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogTagRelationService blogTagRelationService;

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
        page.setSize(Long.parseLong(params.get("limit").toString()));

        //搜索功能
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like("blog_title","%"+keyword+"%")
                   .or(qw -> qw.like("blog_category_name","%"+keyword+"%"));
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
    @Transactional
    public boolean deleteBlog(ArrayList<Integer> list) {
        // 1.删除博客-标签关系表中的记录
        QueryWrapper<BlogTagRelation> blogTagRelationQueryWrapper = new QueryWrapper<>();
        blogTagRelationQueryWrapper.in("blog_id",list);
        blogTagRelationService.remove(blogTagRelationQueryWrapper);

        // 2.删除这些博客的评论
        QueryWrapper<BlogComment> blogCommentQueryWrapper = new QueryWrapper<>();
        blogCommentQueryWrapper.in("blog_id",list);
        commentService.remove(blogCommentQueryWrapper);

        // 3.分类数量-1
        UpdateWrapper<BlogCategory> blogCategoryUpdateWrapper = new UpdateWrapper<>();

        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.in("blog_id",list);
        List<Blog> blogs = baseMapper.selectList(blogQueryWrapper);
        blogs.stream().forEach(blog -> {
            BlogCategory blogCategory = categoryService.getById(blog.getBlogCategoryId());
            blogCategory.setCategoryRank(blogCategory.getCategoryRank()-1);
            categoryService.updateById(blogCategory);
        });

        // 4.删除博客
        baseMapper.deleteBatchIds(list);
        return true;
    }

    @Override
    public BlogDetailVO getBlogDetailBySubUrl(String subUrl) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_sub_url",subUrl)
               .eq("blog_status",1);

        Blog blog = baseMapper.selectOne(wrapper);

        return getBlogDetailVO(blog);
    }


    @Override
    public PageResult getBlogsForIndexPage(int pageNum) {
        Page<Blog> page = new Page<>();
        page.setSize(8).setCurrent(pageNum);
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_status",1);
        wrapper.orderByDesc("blog_id");

        baseMapper.selectPage(page,wrapper);
        List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(page.getRecords());


        PageResult pageResult = new PageResult(page.getTotal(),page.getSize(),page.getPages(),page.getCurrent(),blogListVOS);

        return pageResult;
    }


    private List<BlogListVO> getBlogListVOsByBlogs(List<Blog> blogList) {
        List<BlogListVO> blogListVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(blogList)) {
            List<Integer> categoryIds = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
            Map<Integer, String> blogCategoryMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(categoryIds)) {

                List<BlogCategory> blogCategories = categoryService.selectBatchIds(categoryIds);
                if (!CollectionUtils.isEmpty(blogCategories)) {
                    blogCategoryMap = blogCategories.stream().collect(Collectors.toMap(BlogCategory::getCategoryId, BlogCategory::getCategoryIcon, (key1, key2) -> key2));
                }
            }
            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                BeanUtils.copyProperties(blog, blogListVO);
                if (blogCategoryMap.containsKey(blog.getBlogCategoryId())) {
                    blogListVO.setBlogCategoryIcon(blogCategoryMap.get(blog.getBlogCategoryId()));
                } else {
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/admin/dist/img/category/00.png");
                }
                blogListVOS.add(blogListVO);
            }
        }
        return blogListVOS;
    }

    @Override
    public PageResult getBlogsPageBySearch(String keyword, int pageNum) {
        if (pageNum > 0 && PatternUtil.validKeyword(keyword)) {
            Page<Blog> page = new Page<>();
            page.setCurrent(pageNum)
                .setSize(9);
            QueryWrapper<Blog> wrapper = new QueryWrapper<>();
            wrapper.like("blog_title",keyword);
            wrapper.eq("blog_status",1);
            wrapper.orderByDesc("blog_id");

            baseMapper.selectPage(page,wrapper);
            List<Blog> records = page.getRecords();
            List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(records);
            PageResult pageResult = new PageResult( page.getTotal(), page.getSize(), page.getPages(),page.getCurrent(), blogListVOS);
            return pageResult;
        }
        return null;
    }

    @Override
    public List<SimpleBlogListVO> getBlogListForIndexPage(int type) {
        List<SimpleBlogListVO> simpleBlogListVOS = new ArrayList<>();
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_status",1)
               .last("limit 9");
        if(type == 1) {
            wrapper.orderByDesc("blog_id");
        } else {
            wrapper.orderByDesc("blog_views");
        }

        List<Blog> blogs = baseMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(blogs)) {
            for (Blog blog : blogs) {
                SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                simpleBlogListVOS.add(simpleBlogListVO);
            }
        }
        return simpleBlogListVOS;
    }

    @Override
    public BlogDetailVO getBlogDetail(Long blogId) {
        Blog blog = baseMapper.selectById(blogId);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public PageResult getBlogsPageByTag(String tagName, Integer pageNum) {
        if (PatternUtil.validKeyword(tagName)) {
            BlogTag tag = tagService.selectByTagName(tagName);
            if (tag != null && pageNum > 0) {

                List<Long> blogIds = blogTagRelationService.selectBlogIdsByTagId(tag.getTagId());

                QueryWrapper<Blog> wrapper = new QueryWrapper<>();
                wrapper.in("blog_id",blogIds);
                wrapper.orderByDesc("blog_id");
                wrapper.eq("blog_status",1);

                Page<Blog> page = new Page<>();
                page.setCurrent(pageNum);
                page.setSize(8);

                baseMapper.selectPage(page,wrapper);

                List<BlogListVO> blogListVOS = getBlogListVOsByBlogs(page.getRecords());
                PageResult pageResult = new PageResult(page.getTotal(), page.getSize(), page.getPages(), page.getCurrent(), blogListVOS);
                return pageResult;
            }
        }
        return null;
    }

    /**
     * 方法抽取
     *
     * @param blog
     * @return
     */
    private BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            baseMapper.updateById(blog);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));

            BlogCategory blogCategory = categoryService.selectByPrimaryKey(blog.getBlogCategoryId());

            if (blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            //设置评论数
            Map params = new HashMap();
            params.put("blog_id", blog.getBlogId());
            params.put("comment_status", 1);//过滤审核通过的数据
            blogDetailVO.setCommentCount(commentService.getTotalBlogComments(params));
            return blogDetailVO;
        }
        return null;
    }
}
