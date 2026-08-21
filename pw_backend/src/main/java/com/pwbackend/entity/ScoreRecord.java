package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * 评分记录实体类
 * 对应前端数据库集合：score_rec
 */
@Data
@Document(collection = "score_rec")
public class ScoreRecord {

    @Id
    private String id;

    private String _openid;  // 用户openid
    private String userOpenId;  // 用户openid（兼容字段）
    private String orderId;  // 订单ID
    private String playerId;  // 玩家ID
    private String playerName;  // 玩家昵称
    private String gameType;  // 游戏类型
    private Double score;  // 评分（1-5）
    private String comment;  // 评价内容
    private Date createTime;  // 创建时间
}
