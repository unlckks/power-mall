package com.mingyun.utils;

import cn.hutool.core.lang.Snowflake;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: MingYun
 * @Date: 2023-04-14 19:47
 */
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@RefreshScope
public class MyIdUtil {
    @Value("${my.workId}")
    private Long workId;

    @Value("${my.dataCenterId}")
    private Long dataCenterId;

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(workId, dataCenterId);
    }
}
