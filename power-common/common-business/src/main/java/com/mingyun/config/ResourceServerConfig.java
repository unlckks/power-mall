package com.mingyun.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.HttpConstant;
import com.mingyun.constant.ResourceConstant;
import com.mingyun.filter.TokenTranslateFilter;
import com.mingyun.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

/**
 * @Author: MingYun
 * @Date: 2023-04-03 09:52
 */
@Configuration
@Slf4j
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public TokenTranslateFilter tokenTranslateFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 我们自己搞一个过滤器 放在认证过滤器之前  将token转成认证对象 即可
        http.addFilterBefore(tokenTranslateFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                // 没有带token  绕过网关 直接访问了资源服务器
                .authenticationEntryPoint(authenticationEntryPoint())
                // 有token 但是权限不够 则走这里 403
                .accessDeniedHandler(accessDeniedHandler());
        // 配置放行路径
        http.authorizeRequests()
                .antMatchers(ResourceConstant.RESOURCE_ALLOW_URLS) // 放行路径  /druid/** , /swagger/**, /actuator/**,/custome/**
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    /**
     * 没有带token  绕过网关 401
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(HttpConstant.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstant.UTF_8);
            // 记录一下访问者信息
            String path = request.getRequestURI();
            String ip = request.getRemoteAddr();
            log.error("非法的访问，路径为:{},ip为:{}", path, ip);
            Result<Object> result = Result.fail(BusinessEnum.UN_AUTHORIZATION);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    /**
     * 有token 但是权限不够 则走这里 403
     * controller加了这个权限标记
     *
     * @return
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(HttpConstant.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstant.UTF_8);
            // 记录一下访问者信息
            String path = request.getRequestURI();
            String ip = request.getRemoteAddr();
            log.error("权限不足，路径为:{},ip为:{}", path, ip);
            // 403    -1
            Result<Object> result = Result.fail(BusinessEnum.ACCESS_DENY_FAIL);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };

    }

}
