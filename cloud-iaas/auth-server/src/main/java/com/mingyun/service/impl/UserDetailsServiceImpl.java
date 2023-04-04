package com.mingyun.service.impl;

import com.mingyun.constant.AuthConstant;
import com.mingyun.factory.LoginStrategyFactory;
import com.mingyun.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:02
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory ;

    /**
     * 扩展框架
     * b2c模式
     * 管理员登录
     * 会员登录
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginType = request.getHeader(AuthConstant.LOGIN_TYPE);
        if (!StringUtils.hasText(loginType)){
            throw  new InternalAuthenticationServiceException("非法登录,类型不匹配");
        }
        //进行策略模式
        LoginStrategy instance = loginStrategyFactory.getInstance(loginType);
        if(ObjectUtils.isEmpty(instance)){
            throw new InternalAuthenticationServiceException("非法登录,类型不匹配");
        }
        return instance.realLogin(username);
    }
}
