package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品/服务实体类
 * 对应前端数据库集合：items
 */
@Data
@Document(collection = "items")
public class Item {

    @Id
    private String id;

    private String title;  // 商品标题
    private String describe;  // 商品描述（简短）
    private String detail;  // 商品详情（完整描述）
    private String price;  // 商品价格（字符串格式）
    private String sales;  // 销量
    private String game;  // 游戏类型：cs2, val, delta, lol
    private List<String> categories;  // 分类列表
    private Map<String, Double> rules;  // 购买规则：{buyOnlyOnce: 1.0, buyOncePerDay: 0.0}
}
