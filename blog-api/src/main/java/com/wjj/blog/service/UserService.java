package com.wjj.blog.service;

import com.wjj.blog.dao.pojo.SysUser;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.UserVo;
import org.springframework.stereotype.Service;


public interface UserService {
    UserVo findUserVoById(Long id);

    SysUser findSysUserById(Long userId);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser checkToken(String token);
    //根据账户查找用户
    SysUser findUserByAccount(String account);
    //保存用户操作
    void save(SysUser sysUser);


}
