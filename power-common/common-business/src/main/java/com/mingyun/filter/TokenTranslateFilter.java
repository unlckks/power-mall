package com.mingyun.filter;

import com.alibaba.fastjson.JSON;
import com.mingyun.constant.AuthConstant;

import com.mingyun.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Author: MingYun
 * @Date: 2023-04-03 09:54
 */
@Component
public class TokenTranslateFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 解析token
     * 将有token的请求 转换出来
     * 没有token的请求 不处理 交给框架来处理  如果框架配置了这个api的放行 则直接访问
     * 如果框架没有放行这个api 走框架的authenticationEntryPoint 返回401
     * 1.拿token值
     * 2.做转换
     * 3.放在security的context中去
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(AuthConstant.AUTHORIZATION);
        if (StringUtils.hasText(authorization)) {
            String token = authorization.replaceFirst(AuthConstant.BEARER, "");
            if (StringUtils.hasText(token)) {
                // 判断续约
                Long expire = redisTemplate.getExpire(AuthConstant.LOGIN_TOKEN_PREFIX + token, TimeUnit.SECONDS);
                if (expire < AuthConstant.TOKEN_EXPIRE_THRESHOLD_TIME) {
                    redisTemplate.expire(AuthConstant.LOGIN_TOKEN_PREFIX + token, Duration.ofSeconds(AuthConstant.TOKEN_TIME));
                }
                // 转换
                String userStr = redisTemplate.opsForValue().get(AuthConstant.LOGIN_TOKEN_PREFIX + token);
                SecurityUser securityUser = JSON.parseObject(userStr, SecurityUser.class);
                // 处理权限
                Set<SimpleGrantedAuthority> grantedAuthorities = securityUser.getPerms()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
                // threadLocal
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
