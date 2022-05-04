package com.wjj.blog.api;

import com.wjj.blog.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
//        SysUser sysUser = UserThreadLocal.get();
//        System.out.println(sysUser);
        return Result.success(null);
    }
}
