package com.mingyun.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@RefreshScope
@ConfigurationProperties(prefix = "wx.auth")
public class WxAuthConfig {

    private String appId;
    private String appSecret;
    private String url;


}
