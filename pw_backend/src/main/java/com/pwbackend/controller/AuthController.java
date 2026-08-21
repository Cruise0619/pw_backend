package com.pwbackend.controller;

import com.pwbackend.entity.User;
import com.pwbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器 - 处理微信登录
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${wx.appid:}")
    private String appid;

    @Value("${wx.secret:}")
    private String secret;

    /**
     * 微信登录 - 用code换取openid
     * 如果未配置appid/secret，则使用code模拟openid（本地开发模式）
     */
    @PostMapping("/wx-login")
    public ResponseEntity<Map<String, Object>> wxLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        if (code == null || code.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "code不能为空");
            return ResponseEntity.badRequest().body(error);
        }

        String openid;
        // 本地开发模式：没有配置微信appid时，用code作为openid
        if (appid == null || appid.isEmpty() || "your_appid".equals(appid)) {
            openid = "dev_" + code.replaceAll("[^a-zA-Z0-9]", "").substring(0, Math.min(16, code.length()));
        } else {
            // 正式模式：调用微信API换取openid
            try {
                String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid
                        + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
                Map<String, Object> wxResp = restTemplate.getForObject(url, Map.class);
                if (wxResp != null && wxResp.get("openid") != null) {
                    openid = (String) wxResp.get("openid");
                } else {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("message", "微信登录失败: " + (wxResp != null ? wxResp.get("errmsg") : "未知错误"));
                    return ResponseEntity.badRequest().body(error);
                }
            } catch (Exception e) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "微信API调用失败: " + e.getMessage());
                return ResponseEntity.badRequest().body(error);
            }
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
}
