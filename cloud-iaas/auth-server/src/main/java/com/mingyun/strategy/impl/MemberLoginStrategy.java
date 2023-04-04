package com.mingyun.strategy.impl;

import com.mingyun.strategy.LoginStrategy;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:07
 */
public class MemberLoginStrategy implements LoginStrategy {
    @Override
    public UserDetails realLogin(String username) {
        System.out.println("会员策略");
        return null;
    }
}
