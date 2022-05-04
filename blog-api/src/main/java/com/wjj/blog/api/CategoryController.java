package com.wjj.blog.api;

import com.wjj.blog.service.CategoryService;
import com.wjj.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result categories(){
        //提供所有的类别查询
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoriesDetail(){
        //提供所有的类别查询
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }
}
