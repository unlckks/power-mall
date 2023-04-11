package com.mingyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: 动力节点·武汉
 * 时间: 2023-04-11 09:23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MsgServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(MsgServiceApp.class, args);
    }
}
