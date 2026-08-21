package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * 客服实体类
 * 对应前端数据库集合：ops
 */
@Data
@Document(collection = "ops")
public class Ops {

    @Id
    private String id;  // MongoDB _id，与openid相同

    private String _openid;  // 微信openid（兼容字段）
    private String openid;  // 微信openid
    private String nickname;  // 客服昵称
    private String avatarUrl;  // 客服头像URL
    private Date registerTime;  // 注册时间
}
