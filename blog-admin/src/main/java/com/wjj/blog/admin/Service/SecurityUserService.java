package com.wjj.blog.admin.Service;

import com.wjj.blog.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class SecurityUserService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //登录时，把username传递到这里
        //通过username查询admin表，如果admin存在 将密码告诉我们的spring security
        //不存在，返回null 认证失败
        Admin admin = this.adminService.findAdminByUserName(username);
        if (admin == null) {
            return null;
            //登录失败
        }
        UserDetails userDetails = new User(username,admin.getPassword(),new ArrayList<>());
        return userDetails;
     }
}
