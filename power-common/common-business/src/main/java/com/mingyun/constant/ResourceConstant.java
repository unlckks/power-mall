package com.mingyun.constant;

/**
 * @Author: MingYun
 * @Date: 2023-04-03 13:59
 */
public interface ResourceConstant {

    String[] RESOURCE_ALLOW_URLS = {
            // swagger  druid ...
            "/v2/api-docs",
            "/v3/api-docs",
            //用来获取支持的动作
            "/swagger-resources/configuration/ui",
            //用来获取api-docs的URI
            "/swagger-resources",
            //安全选项
            "/swagger-resources/configuration/security",
            "/webjars/**",
            "/swagger-ui/**",
            "/druid/**",
            "/actuator/**"
    };
}
