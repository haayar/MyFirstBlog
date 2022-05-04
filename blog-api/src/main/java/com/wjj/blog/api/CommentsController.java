package com.wjj.blog.api;

import com.wjj.blog.service.CommentsService;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;
    //comments/article/{id}
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }


    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        System.out.println(commentParam.getArticleId());
        System.out.println(commentParam.getContent());
        System.out.println(commentParam.getParent());
        System.out.println(commentParam.getToUserId());
        return commentsService.comment(commentParam);
    }
}
