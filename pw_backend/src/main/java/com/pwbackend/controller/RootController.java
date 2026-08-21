package com.pwbackend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 根路径控制器 - 提供API欢迎信息和可用端点列表
 */
@RestController
@CrossOrigin(origins = "*")
public class RootController {

    /**
     * 根路径 - 返回API欢迎信息
     */
    @GetMapping("/")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "欢迎使用电竞陪玩点单小程序后端服务");
        response.put("service", "电竞陪玩点单小程序 Backend API");
        response.put("version", "1.0.0");
        response.put("adminUrl", "http://localhost:8081/admin.html");

        Map<String, String> apiEndpoints = new HashMap<>();
        apiEndpoints.put("用户相关", "/api/user/*");
        apiEndpoints.put("商品相关", "/api/items/*");
        apiEndpoints.put("订单相关", "/api/orders/*");
        apiEndpoints.put("陪玩玩家", "/api/players/*");
        apiEndpoints.put("评分相关", "/api/scores/*");
        apiEndpoints.put("分类相关", "/api/categories/*");
        apiEndpoints.put("首页数据", "/api/index/data");
        apiEndpoints.put("折扣信息", "/api/discounts/*");
        apiEndpoints.put("数据导入", "/api/import/*");
        apiEndpoints.put("客服相关", "/api/ops/*");

        response.put("availableApis", apiEndpoints);

        Map<String, String> examples = new HashMap<>();
        examples.put("管理后台", "GET http://localhost:8081/admin.html");
        examples.put("获取首页数据", "GET http://localhost:8081/api/index/data");
        examples.put("获取所有商品", "GET http://localhost:8081/api/items");
        examples.put("获取用户信息", "GET http://localhost:8081/api/user/{openid}");
        examples.put("创建订单", "POST http://localhost:8081/api/orders");

        response.put("examples", examples);

        return response;
    }
}
