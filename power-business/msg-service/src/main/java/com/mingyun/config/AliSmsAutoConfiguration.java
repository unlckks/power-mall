package com.mingyun.config;

import com.alibaba.spring.beans.factory.annotation.EnableConfigurationBeanBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:01
 */
@Configuration
@EnableConfigurationProperties(AliSmsProperties.class)
public class AliSmsAutoConfiguration {
    @Autowired
    private AliSmsProperties aliSmsProperties ;
    @Bean
    public com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(aliSmsProperties.getAk())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(aliSmsProperties.getSk());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}
