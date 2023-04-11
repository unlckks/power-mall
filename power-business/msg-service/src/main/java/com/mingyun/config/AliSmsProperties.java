package com.mingyun.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RefreshScope
@ConfigurationProperties(prefix = "ali.sms")
public class AliSmsProperties {

    private String ak;
    private String sk;

}
