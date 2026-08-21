package com.pwbackend.controller;

import com.pwbackend.service.DiscountService;
import com.pwbackend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 */
@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IndexController {

    private final DiscountService discountService;
    private final ItemService itemService;

    @Value("${image.base-url:http://localhost:8081/storage}")
    private String imageBaseUrl;

    /**
     * 获取首页数据（轮播图、推荐商品、折扣信息）
     */
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getIndexData() {
        // 轮播图数据（使用本地存储路径）
        Map<String, Object> result = new HashMap<>();

        result.put("swiperList", new Object[]{
            Map.of(
                "id", 1,
                "image", imageBaseUrl + "/swiper/swiper1.jpg",
                "text", "开业福利，下单享8折优惠"
            ),
            Map.of(
                "id", 2,
                "image", imageBaseUrl + "/swiper/swiper2.jpg",
                "text", "认证陪玩，专业服务，技术保障"
            ),
            Map.of(
                "id", 3,
                "image", imageBaseUrl + "/swiper/swiper3.jpg",
                "text", "各路巅峰大神带你得吃"
            )
        });

        // 随机推荐商品
        result.put("randomItems", itemService.getRandomItems(6));

        // 当前折扣
        result.put("discount", discountService.getActiveDiscount());

        return ResponseEntity.ok(result);
    }
}
