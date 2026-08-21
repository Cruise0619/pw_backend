package com.pwbackend.service;

import com.pwbackend.entity.Discount;
import com.pwbackend.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 折扣服务层 - MongoDB版本
 */
@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 获取所有折扣
     */
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    /**
     * 获取当前激活的折扣
     */
    public Map<String, Object> getActiveDiscount() {
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true));
        List<Discount> discounts = mongoTemplate.find(query, Discount.class);

        if (discounts.isEmpty()) {
            return null;
        }

        Discount discount = discounts.get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("name", discount.getName());
        result.put("payPercent", discount.getPayPercent());

        return result;
    }

    /**
     * 根据名称获取折扣详情
     */
    public Optional<Discount> getDiscountByName(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Discount> discounts = mongoTemplate.find(query, Discount.class);
        return discounts.isEmpty() ? Optional.empty() : Optional.of(discounts.get(0));
    }

    /**
     * 创建或更新折扣
     */
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    /**
     * 删除折扣
     */
    public void deleteDiscount(String id) {
        discountRepository.deleteById(id);
    }

    /**
     * 激活指定折扣
     */
    public void activateDiscount(String discountId) {
        // 先禁用所有折扣
        Query query = new Query();
        query.addCriteria(Criteria.where("active").is(true));
        List<Discount> discounts = mongoTemplate.find(query, Discount.class);
        discounts.forEach(d -> {
            d.setActive(false);
            discountRepository.save(d);
        });

        // 激活指定折扣
        discountRepository.findById(discountId).ifPresent(discount -> {
            discount.setActive(true);
            discountRepository.save(discount);
        });
    }
}
