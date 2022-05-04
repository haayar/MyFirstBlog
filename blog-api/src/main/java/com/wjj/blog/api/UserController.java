package com.wjj.blog.api;

import com.wjj.blog.service.UserService;
import com.wjj.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    @Lazy
    private UserService userService;
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        System.out.println(token);
    return userService.findUserByToken(token);
    }
}
