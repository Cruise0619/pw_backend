package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.Discount;
import com.pwbackend.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 折扣控制器 - MongoDB版本
 */
@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiscountController {

    private final DiscountService discountService;

    /**
     * 获取所有折扣
     */
    @GetMapping
    public ApiResponse<List<Discount>> getAllDiscounts() {
        return ApiResponse.success(discountService.getAllDiscounts());
    }

    /**
     * 获取当前激活的折扣
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveDiscount() {
        Map<String, Object> discount = discountService.getActiveDiscount();

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", discount);

        return ResponseEntity.ok(result);
    }

    /**
     * 激活折扣
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<Map<String, Object>> activateDiscount(@PathVariable String id) {
        try {
            discountService.activateDiscount(id);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "折扣已激活");

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 创建/更新折扣
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveDiscount(@RequestBody Discount discount) {
        Discount saved = discountService.saveDiscount(discount);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", saved);

        return ResponseEntity.ok(result);
    }

    /**
     * 删除折扣
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDiscount(@PathVariable String id) {
        discountService.deleteDiscount(id);
        return ApiResponse.success(null);
    }
}
