package com.mingyun.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author: MingYun
 * @Date: 2023-04-03 09:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "swagger3")
public class SwaggerProperties {

    /**
     * 要对那个包的Controller 做文档的生成
     */
    private String basePackage;
    /**
     * 作者的名称
     */
    private String name;
    /**
     * 主页
     */
    private String url;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 服务的团队
     */
    private String termsOfServiceUrl;
    /**
     * 授权信息
     */
    private String license;
    /**
     * 授权的url
     */
    private String licenseUrl;
    /**
     * 版本
     */
    private String version;


}
