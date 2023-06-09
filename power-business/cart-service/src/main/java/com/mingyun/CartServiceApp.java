package com.mingyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:25
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CartServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(CartServiceApp.class, args);
    }
}
