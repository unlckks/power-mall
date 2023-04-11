package com.mingyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @Author: MingYun
 * @Date: 2023-04-10 20:58
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class StoreServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApp.class, args);
    }
}
