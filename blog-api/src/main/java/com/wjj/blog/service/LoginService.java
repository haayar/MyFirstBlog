package com.wjj.blog.service;

import com.wjj.blog.dao.pojo.SysUser;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.params.LoginParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional//添加事务注册要确保生效
public interface LoginService {
    //登录
    Result login(LoginParam loginParam);

    //退出
    Result logout(String token);
    //注册
    Result register(LoginParam loginParam);

}
