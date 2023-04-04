package com.mingyun.strategy;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:06
 */
public interface LoginStrategy {
    UserDetails realLogin(String username);
}
