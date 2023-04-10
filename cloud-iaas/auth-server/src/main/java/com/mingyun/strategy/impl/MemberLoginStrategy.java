package com.mingyun.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mingyun.config.WxAuthConfig;
import com.mingyun.constant.AuthConstant;
import com.mingyun.domain.LoginMember;
import com.mingyun.mapper.LoginMemberMapper;
import com.mingyun.model.SecurityUser;
import com.mingyun.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:07
 */
@Service(AuthConstant.MEMBER_LOGIN)
public class MemberLoginStrategy implements LoginStrategy {


    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private WxAuthConfig wxAuthConfig;

    @Autowired
    private LoginMemberMapper loginMemberMapper;

    /**
     * 小程序登录
     *
     * @param username
     * @return
     */
    @Override
    public UserDetails realLogin(String username) {
        System.out.println("会员策略");
        String realWxAuthUrl = String.format(wxAuthConfig.getUrl(), wxAuthConfig.getAppId(), wxAuthConfig.getAppSecret(), username);
        String wxAuthStr = restTemplate.getForObject(realWxAuthUrl, String.class);
        JSONObject jsonObject = JSON.parseObject(wxAuthStr);
        String openId = jsonObject.getString("openid");
        if (!StringUtils.hasText(openId)) {
            throw new InternalAuthenticationServiceException("登录异常,重新进入");
        }
        //拿用户的身份信息, 有则返回,没就注册
        LoginMember loginMember = loginMemberMapper.selectOne(new LambdaQueryWrapper<LoginMember>()
                .eq(LoginMember::getOpenId, openId)
        );
        if (ObjectUtils.isEmpty(loginMember)) {
            //进行注册
            loginMember = registerMember(openId);
        }
        //统一封装返回结果
        SecurityUser securityUser = new SecurityUser();
        securityUser.setStatus(loginMember.getStatus());
        securityUser.setUserId(loginMember.getId());
        securityUser.setLoginType(AuthConstant.MEMBER_LOGIN);
        securityUser.setUsername(openId);
        securityUser.setOpenId(openId);
        securityUser.setPassword("$2a$10$BeeAikeHdJTRf67HQnZqE..2Un7IHJ0S/gD.q30rRqGAagiYu4XdG");
        return securityUser ;
    }

    /**
     * 进行注册
     *
     * @param openId
     * @return
     */
    private LoginMember registerMember(String openId) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        LoginMember loginMember = new LoginMember();
        loginMember.setOpenId(openId);
        loginMember.setOpenId(openId);
        loginMember.setCreateTime(new Date());
        loginMember.setUpdateTime(new Date());
        loginMember.setStatus(1);
        loginMember.setUserLasttime(new Date());
        // 业务处理
        loginMember.setScore(0);
        loginMember.setUserRegip(request.getRemoteAddr());
        loginMember.setUserLastip(request.getRemoteAddr());
        loginMemberMapper.insert(loginMember);
        return loginMember;

    }
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("WECHAT"));
    }
}

