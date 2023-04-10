package com.mingyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: MingYun
 * @Date: 2023-04-01 19:08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class,args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
