package com.wjj.blog.admin.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjj.blog.admin.mapper.AdminMapper;
import com.wjj.blog.admin.pojo.Admin;
import com.wjj.blog.admin.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public Admin findAdminByUserName(String username){
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,username);
        queryWrapper.last("limit 1");

        Admin admin = (Admin) adminMapper.selectOne(queryWrapper);
        return admin;
    }

    public List<Permission> findPermissionByAdminId(Long adminId) {
        //select * from 'ms_permission' where id in (select permissionId from admin_ms_permission where admin_id =1)
        return adminMapper.findPermissionByAdminId(adminId);
    }
}
