package com.mingyun.factory;

import com.mingyun.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:04
 */
@Component
public class LoginStrategyFactory {
    @Autowired
    private Map<String,LoginStrategy> loginStrategyMap = new HashMap<>();
    /**
     * 根据类型返回不同的策略
     *
     */
    public LoginStrategy getInstance(String loginType){
        return loginStrategyMap.get(loginType);
    }
}
