package com.mingyun.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mingyun.constant.AuthConstant;
import com.mingyun.constant.BusinessEnum;
import com.mingyun.constant.HttpConstant;
import com.mingyun.model.LoginResult;
import com.mingyun.model.Result;
import com.mingyun.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.UUID;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:04
 */
@Configuration
@Slf4j
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 自定义的认证流程
     * 告诉框架 走我们自己的登录逻辑
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 网络请求的配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭跨站请求伪造的拦截
        http.csrf().disable();
        http.cors().disable();
        // session  STATELESS的策略
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 配置登录的信息
        http.formLogin()
                .loginProcessingUrl(AuthConstant.LOGIN_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());
        // 登出的配置
        http.logout()
                .logoutUrl(AuthConstant.LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler());
        // 其它所有接口 都必须认证后才可以访问 （必须在 securityContext 中有认证对象 才可以访问）
        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    /**
     * 登录成功的处理器
     * 生成一个token
     * 当前的认证对象
     * 存入redis
     * 返回token
     *
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType(HttpConstant.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstant.UTF_8);
            String token = UUID.randomUUID().toString();
            // authentication.getPrincipal() 就是securityUser
            String userStr = JSON.toJSONString(authentication.getPrincipal());
            stringRedisTemplate.opsForValue().set(AuthConstant.LOGIN_TOKEN_PREFIX + token, userStr, Duration.ofSeconds(AuthConstant.TOKEN_TIME));
            LoginResult loginResult = new LoginResult(token, AuthConstant.TOKEN_TIME);
            Result<LoginResult> result = Result.success(loginResult);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    /**
     * 登录失败的处理器
     *
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            log.error("登录失败:{}", exception.getMessage());
            response.setContentType(HttpConstant.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstant.UTF_8);
            Result<String> result = new Result<>();
            result.setCode(BusinessEnum.UN_AUTHORIZATION.getCode());
            if (exception instanceof BadCredentialsException) {
                result.setMsg("用户名或者密码错误");
            } else if (exception instanceof UsernameNotFoundException) {
                result.setMsg("用户名错误");
            } else if (exception instanceof AccountExpiredException) {
                result.setMsg("账号异常，请联系管理员");
            } else if (exception instanceof InternalAuthenticationServiceException) {
                result.setMsg(exception.getMessage());
            } else {
                result.setMsg(BusinessEnum.UN_AUTHORIZATION.getDesc());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }

    /**
     * 登出的处理器
     * redis删除
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType(HttpConstant.APPLICATION_JSON);
            response.setCharacterEncoding(HttpConstant.UTF_8);
            String authorization = request.getHeader(AuthConstant.AUTHORIZATION);
            String token = authorization.replaceFirst(AuthConstant.BEARER, "");
            stringRedisTemplate.delete(AuthConstant.LOGIN_TOKEN_PREFIX + token);
            Result<Object> result = Result.success(null);
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(result);
            PrintWriter writer = response.getWriter();
            writer.write(s);
            writer.flush();
            writer.close();
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
