package com.pwbackend.controller;

import com.pwbackend.entity.User;
import com.pwbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    /**
     * 获取所有用户
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 用户登录/注册
     * 前端通过code换取openid后调用此接口
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String openid = request.get("openid");

        if (openid == null || openid.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "openid不能为空"));
        }

        // 获取或创建用户
        User user = userService.getOrCreateUser(openid);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("userId", user.getOpenid());
        result.put("userInfo", user);
        result.put("isOps", userService.isOps(openid));

        return ResponseEntity.ok(result);
    }

    /**
     * 获取用户信息（用户不存在则自动创建）
     */
    @GetMapping("/{openid}")
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable String openid) {
        User user = userService.getOrCreateUser(openid);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("userInfo", user);
        result.put("isOps", userService.isOps(openid));
        return ResponseEntity.ok(result);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{openid}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String openid,
            @RequestBody Map<String, String> request) {

        try {
            User user = userService.updateUser(
                openid,
                request.get("nickname"),
                request.get("avatarUrl"),
                request.get("phone")
            );

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("userInfo", user);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 检查用户是否为客服
     */
    @GetMapping("/{openid}/isOps")
    public ResponseEntity<Map<String, Object>> checkIsOps(@PathVariable String openid) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("isOps", userService.isOps(openid));

        return ResponseEntity.ok(result);
    }
}
