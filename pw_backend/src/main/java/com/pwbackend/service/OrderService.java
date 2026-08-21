package com.pwbackend.service;

import com.pwbackend.entity.Order;
import com.pwbackend.entity.Item;
import com.pwbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订单服务层
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final MongoTemplate mongoTemplate;

    /**
     * 获取所有订单
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 根据ID获取订单
     */
    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据用户openid获取订单
     */
    public List<Order> getOrdersByOpenid(String openid) {
        return orderRepository.findByOpenid(openid);
    }

    /**
     * 根据用户openid和状态获取订单
     */
    public List<Order> getOrdersByOpenidAndStatus(String openid, String status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("openid").is(openid));
        query.addCriteria(Criteria.where("status").is(status));
        return mongoTemplate.find(query, Order.class);
    }

    /**
     * 根据状态获取订单
     */
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * 根据玩家ID获取相关订单
     */
    public List<Order> getOrdersByPlayerId(String playerId) {
        return orderRepository.findByPlayersContaining(playerId);
    }

    /**
     * 创建订单
     */
    public Order createOrder(Order order) {
        order.setCreateTime(new Date());
        return orderRepository.save(order);
    }

    /**
     * 更新订单状态
     */
    public Order updateOrderStatus(String id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 更新订单支付状态
     */
    public Order updateOrderPayStatus(String id, boolean paid) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setWaitingForWXPay(paid ? 0 : 1);
            order.setStatus(paid ? "pre" : "待支付");
            if (paid) {
                order.setPayTime(new Date());
            }
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 取消订单
     */
    public Order cancelOrder(String id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("已取消");
            order.setWaitingForCancelCorfirm(0);
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 更新订单任意字段
     */
    public Order updateOrderFields(String id, Map<String, Object> fields) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (fields.containsKey("status")) order.setStatus((String) fields.get("status"));
            if (fields.containsKey("items")) order.setItems((List<Order.OrderItem>) fields.get("items"));
            if (fields.containsKey("totalFee")) order.setTotalFee(new java.math.BigDecimal(fields.get("totalFee").toString()));
            if (fields.containsKey("waitingForCustomStart")) order.setWaitingForCustomStart((Integer) fields.get("waitingForCustomStart"));
            if (fields.containsKey("waitingForCustomCompelete")) order.setWaitingForCustomCompelete((Integer) fields.get("waitingForCustomCompelete"));
            if (fields.containsKey("waitingForPlayerStart")) order.setWaitingForPlayerStart((Integer) fields.get("waitingForPlayerStart"));
            if (fields.containsKey("waitingForPlayerCompelete")) order.setWaitingForPlayerCompelete((Integer) fields.get("waitingForPlayerCompelete"));
            if (fields.containsKey("waitingForCancelCorfirm")) order.setWaitingForCancelCorfirm((Integer) fields.get("waitingForCancelCorfirm"));
            if (fields.containsKey("waitingForWXPay")) order.setWaitingForWXPay((Integer) fields.get("waitingForWXPay"));
            if (fields.containsKey("waitingForScore")) order.setWaitingForScore((Integer) fields.get("waitingForScore"));
            if (fields.containsKey("isScored")) order.setIsScored((Boolean) fields.get("isScored"));
            if (fields.containsKey("scoreTime")) order.setScoreTime(new Date());
            if (fields.containsKey("payTime")) order.setPayTime(new Date());
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 保存订单
     */
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
