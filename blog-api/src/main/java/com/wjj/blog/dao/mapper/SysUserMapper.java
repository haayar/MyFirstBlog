package com.wjj.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjj.blog.dao.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
}
