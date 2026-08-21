package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.Order;
import com.pwbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    /**
     * 获取所有订单
     */
    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        return ApiResponse.success(orderService.getAllOrders());
    }

    /**
     * 根据ID获取订单
     */
    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("订单不存在"));
    }

    /**
     * 根据用户openid获取订单
     */
    @GetMapping("/user/{openid}")
    public ApiResponse<List<Order>> getOrdersByOpenid(@PathVariable String openid) {
        return ApiResponse.success(orderService.getOrdersByOpenid(openid));
    }

    /**
     * 根据用户openid和状态获取订单
     */
    @GetMapping("/user/{openid}/status/{status}")
    public ApiResponse<List<Order>> getOrdersByOpenidAndStatus(
            @PathVariable String openid,
            @PathVariable String status) {
        return ApiResponse.success(orderService.getOrdersByOpenidAndStatus(openid, status));
    }

    /**
     * 根据状态获取订单
     */
    @GetMapping("/status/{status}")
    public ApiResponse<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ApiResponse.success(orderService.getOrdersByStatus(status));
    }

    /**
     * 根据玩家ID获取相关订单
     */
    @GetMapping("/player/{playerId}")
    public ApiResponse<List<Order>> getOrdersByPlayerId(@PathVariable String playerId) {
        return ApiResponse.success(orderService.getOrdersByPlayerId(playerId));
    }

    /**
     * 创建订单
     */
    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody Order order) {
        return ApiResponse.success(orderService.createOrder(order));
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    /**
     * 更新订单支付状态
     */
    @PutMapping("/{id}/pay")
    public ApiResponse<Order> updateOrderPayStatus(
            @PathVariable String id,
            @RequestParam boolean paid) {
        Order order = orderService.updateOrderPayStatus(id, paid);
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    /**
     * 更新订单任意字段
     */
    @PutMapping("/{id}/fields")
    public ApiResponse<Order> updateOrderFields(
            @PathVariable String id,
            @RequestBody Map<String, Object> fields) {
        Order order = orderService.updateOrderFields(id, fields);
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public ApiResponse<Order> cancelOrder(@PathVariable String id) {
        Order order = orderService.cancelOrder(id);
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    /**
     * 保存订单
     */
    @PutMapping
    public ApiResponse<Order> saveOrder(@RequestBody Order order) {
        return ApiResponse.success(orderService.saveOrder(order));
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ApiResponse.success(null);
    }
}
