package com.wjj.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjj.blog.dao.dos.Archives;
import com.wjj.blog.dao.mapper.ArticleBodyMapper;
import com.wjj.blog.dao.mapper.ArticleMapper;
import com.wjj.blog.dao.mapper.ArticleTagMapper;
import com.wjj.blog.dao.pojo.Article;
import com.wjj.blog.dao.pojo.ArticleBody;
import com.wjj.blog.dao.pojo.ArticleTag;
import com.wjj.blog.dao.pojo.SysUser;
import com.wjj.blog.service.*;

import com.wjj.blog.utils.UserThreadLocal;
import com.wjj.blog.vo.ArticleBodyVo;
import com.wjj.blog.vo.ArticleVo;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.TagVo;
import com.wjj.blog.vo.params.ArticleParam;
import com.wjj.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticlesPage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());

        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

//    @Override
//    public List<ArticleVo> listArticlesPage(PageParams pageParams) {
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null){
//            //and category_id = #{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage =articleMapper.selectPage(page,queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        for (int i = 0; i < records.size(); i++) {
//            System.out.println(records.get(i));
//        }
//        System.out.println("===========================");
//
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        for (int i = 0; i < articleVoList.size(); i++) {
//            System.out.println(articleVoList.get(i));
//        }
//        System.out.println("=====================================");
//        return articleVoList;
//    }



    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit " + limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList=articleMapper.listArchives();
        return Result.success(archivesList);

    }
    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        /*
        1.根据id查询文章信息
        2.根据bodyId和categoryid 去做关联查询
         */
        Article article =this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true,true,true);
        //看完文章后新增阅读次数，本应该直接返回数据了，这时候做一个更新操作，但是更新数据库时，会加一个写锁，会阻塞其他的读操作，性能就会比较低
        //更新增加了此次接口的耗时  如果一旦更新出问题不能影响我们查看操作
        //使用线程池解决 可以把更新操作丢到线程池中操作，与主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        //使用UserThreadLocal的前提是，
        // 此接口必须要加入到登录拦截里头，
        // 否则查找不到用户信息
        SysUser sysUser = UserThreadLocal.get();
        /*
        1.发布文章 目的 构建Article对象
        2.作者id 当前登录用户
        3.标签 要将标签加入到 关联列表当中
        4.body 内容存储
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));

        //插入之后 会生成一个文章id
        this.articleMapper.insert(article);
        //获得tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContent(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());

        articleMapper.updateById(article);

        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVoList =new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList =new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }


    @Autowired
    private CategoryService categoryService;


    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findSysUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
