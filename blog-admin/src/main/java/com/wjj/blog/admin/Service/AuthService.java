package com.wjj.blog.admin.Service;

import com.wjj.blog.admin.pojo.Admin;
import com.wjj.blog.admin.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {
    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest httpServletRequest, Authentication authentication){
        //权限认证
        //请求路径
        String requestURI = httpServletRequest.getRequestURI();
        //当前用户信息
        Object principal = authentication.getPrincipal();
        if (principal == null || "anonymousUser".equals(principal)){
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin =this.adminService.findAdminByUserName(username);
        if (admin==null){
            return false;
        }
        if (admin.getId() == 1) {
            //超级管理员
            return true;
        }
        Long id = admin.getId();
        List<Permission> permissionList = this.adminService.findPermissionByAdminId(id);
        requestURI= StringUtils.split(requestURI,'?')[0];
        System.out.println(requestURI);
        for (Permission permission : permissionList) {
            if (requestURI.equals(permission.getPath())) {
                System.out.println(requestURI);
                System.out.println(permission.getPath());
                return true;
            }else {
                return false;
            }
        }

        return true;
    }
}
