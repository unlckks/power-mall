package com.mingyun.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: MingYun
 * @Date: 2023-04-07 18:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@RefreshScope
@Component
@ConfigurationProperties(prefix = "esimport")
public class EsImportConfig {
    private Boolean falg;
}
