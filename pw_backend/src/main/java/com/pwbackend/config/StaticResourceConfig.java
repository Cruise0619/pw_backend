package com.pwbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源配置
 * 用于提供前端访问 storage 目录下的静态资源
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 使用绝对路径配置静态资源目录
        String storagePath = "D:/pw_backend/storage/";

        // 配置 /storage/** 路径映射到 storage 目录（商品图片等）
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:" + storagePath);

        // 配置 /images/** 路径映射到 storage/images 目录（图标等）
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + storagePath + "images/");

        System.out.println("静态资源路径配置: /storage/** -> " + storagePath);
        System.out.println("静态资源路径配置: /images/** -> " + storagePath + "images/");
    }
}
