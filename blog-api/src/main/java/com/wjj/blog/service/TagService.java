package com.wjj.blog.service;

import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.TagVo;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TagService {
    List<TagVo> findTagsByArticleId(Long id);

    Result hots(int limit);
    //查询所有的文章标签
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
