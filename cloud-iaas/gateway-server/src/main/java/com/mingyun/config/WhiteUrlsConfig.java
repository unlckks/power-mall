package com.mingyun.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 11:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@RefreshScope
@ConfigurationProperties(prefix = "gateway.white")
public class WhiteUrlsConfig {

    private List<String> allowUrls;

}
