package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 支付控制器 - 本地开发模拟微信支付
 */
@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    /**
     * 模拟微信支付下单
     * 在本地开发环境下返回模拟的支付参数
     */
    @PostMapping("/wxpay")
    public ApiResponse<Map<String, Object>> wxpay(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.get("orderId");
        Object totalFeeObj = request.get("totalFee");
        String body = (String) request.get("body");

        // 生成模拟支付参数
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        paymentData.put("nonceStr", UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        paymentData.put("packageVal", "prepay_id=mock_" + UUID.randomUUID().toString().replace("-", ""));
        paymentData.put("paySign", UUID.randomUUID().toString().replace("-", ""));
        paymentData.put("signType", "RSA");
        paymentData.put("orderId", orderId);

        // 注意：在真实环境中无法在本地唤起微信支付
        // 开发模式下支付请求会失败，这是正常的
        // 可以通过修改order.js跳过支付步骤来测试流程

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", paymentData);
        result.put("message", "本地模拟支付参数（开发环境无法真实支付）");

        return ApiResponse.success("模拟支付参数", paymentData);
    }
}
