package com.wjj.blog.service;

import com.wjj.blog.vo.CategoryVo;
import com.wjj.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
