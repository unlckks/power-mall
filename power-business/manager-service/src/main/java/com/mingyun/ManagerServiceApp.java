package com.mingyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: MingYun
 * @Date: 2023-04-04 14:56
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class     ManagerServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ManagerServiceApp.class,args);
    }
}
