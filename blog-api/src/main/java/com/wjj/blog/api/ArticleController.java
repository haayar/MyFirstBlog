package com.wjj.blog.api;

import com.wjj.blog.common.aop.LogAnnotation;
import com.wjj.blog.service.ArticleService;
import com.wjj.blog.vo.ArticleVo;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.params.ArticleParam;
import com.wjj.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    //文章
    @PostMapping
    @LogAnnotation(module="文章",operator="获取文章列表")
    public Result articles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        Result articles = articleService.listArticlesPage(pageParams);

        return articles;
    }
    //最热文章
    @PostMapping("hot")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    //首页最新文章
    @PostMapping("new")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticle(limit);
    }
    //首页最新文章
    @PostMapping("listArchives")
    public Result listArchivesArticle(){

        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);

    }
}
