package com.wjj.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wjj.blog.dao.mapper.SysUserMapper;
import com.wjj.blog.dao.pojo.SysUser;
import com.wjj.blog.service.LoginService;
import com.wjj.blog.service.UserService;
import com.wjj.blog.utils.JWTUtils;
import com.wjj.blog.vo.ErrorCode;
import com.wjj.blog.vo.LoginUserVo;
import com.wjj.blog.vo.Result;
import com.wjj.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired

    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private LoginService loginService;

    @Override
    public UserVo findUserVoById(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("码神之路");

        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findSysUserById(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("码神之路");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /*
        token合法性校验
            是否为空
            解析是否成功
            redis是否存在
        2.如果校验失败
            返回错误
            如果成功，返回对应结果 LoginUserVo
         */
        SysUser sysUser = checkToken(token);
        if (sysUser == null){
            Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setAccount(sysUser.getAccount());
        return Result.success(loginUserVo);
    }
    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");

        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户id 会自动生成
        //默认生成的id是分布式id，采用的雪花算法
        //mybatisPlus
        this.sysUserMapper.insert(sysUser);
    }
}