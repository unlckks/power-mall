package com.mingyun.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.mingyun.constant.AuthConstant;
import com.mingyun.domain.LoginSysUser;
import com.mingyun.mapper.LoginSysUserMapper;
import com.mingyun.model.SecurityUser;
import com.mingyun.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:07
 */
@Service(AuthConstant.SYS_USER_LOGIN)
public class SysUserLoginStrategy implements LoginStrategy {
    @Autowired
    private LoginSysUserMapper loginSysUserMapper;

    /**
     * 后台管理员的登录
     *
     * @param username
     * @return
     */
    @Override
    public UserDetails realLogin(String username) {
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>()
                .eq(LoginSysUser::getUsername, username)
        );
        if (!ObjectUtils.isEmpty(loginSysUser)) {
            SecurityUser securityUser = new SecurityUser();
            // 查询权限
            Set<String> perms = loginSysUserMapper.selectPermsBySysUserId(loginSysUser.getUserId());
            securityUser.setUserId(loginSysUser.getUserId());
            securityUser.setLoginType(AuthConstant.SYS_USER_LOGIN);
            securityUser.setPassword(loginSysUser.getPassword());
            securityUser.setUsername(loginSysUser.getUsername());
            securityUser.setStatus(loginSysUser.getStatus());
            securityUser.setShopId(loginSysUser.getShopId());
            if (!CollectionUtils.isEmpty(perms)) {
                securityUser.setPerms(perms);
            }
            return securityUser;
        }
        return null;
    }
}
