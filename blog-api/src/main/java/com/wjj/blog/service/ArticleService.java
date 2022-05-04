package com.wjj.blog.service;

import com.wjj.blog.vo.ArticleVo;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.params.ArticleParam;
import com.wjj.blog.vo.params.PageParams;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticleService {
    Result listArticlesPage(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticle(int limit);
    //文章归档
    Result listArchives();
    //查看文章详情
    Result findArticleById(Long articleId);
    //发布文章
    Result publish(ArticleParam articleParam);
}
