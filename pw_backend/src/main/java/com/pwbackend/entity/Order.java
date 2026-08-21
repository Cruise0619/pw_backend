package com.pwbackend.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 * 对应前端数据库集合：orders
 */
@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;  // MongoDB自动生成的_id，作为订单ID

    private String openid;  // 用户openid
    private String status;  // 订单状态：pre, 未开始, 待支付, 进行中, 已完成, 已取消
    private String game;  // 游戏类型
    private List<OrderItem> items;  // 商品列表
    private List<String> players;  // 玩家ID列表
    private BigDecimal totalFee;  // 总费用
    private BigDecimal originalFee;  // 原始费用
    private String discount;  // 折扣名称
    private Date createTime = new Date();
    private Date payTime;  // 支付时间
    private Date completeTime;  // 完成时间

    // 待确认状态标志
    private Integer waitingForCustomStart = 0;  // 等待客服开始
    private Integer waitingForCustomCompelete = 0;  // 等待客服完成
    private Integer waitingForPlayerStart = 0;  // 等待玩家开始
    private Integer waitingForPlayerCompelete = 0;  // 等待玩家完成
    private Integer waitingForCancelCorfirm = 0;  // 等待取消确认
    private Integer waitingForWXPay = 0;  // 等待微信支付
    private Integer waitingForScore = 0;  // 等待评分
    private Boolean isScored = false;  // 是否已评分
    private Date scoreTime;  // 评分时间

    /**
     * 订单商品内部类
     */
    @Getter
    @Setter
    public static class OrderItem {
        private String id;
        private Integer num = 1;
    }
}
