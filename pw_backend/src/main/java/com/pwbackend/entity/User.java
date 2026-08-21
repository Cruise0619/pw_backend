package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

/**
 * 用户实体类
 * 对应前端数据库集合：users
 */
@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;  // MongoDB _id，与openid相同

    private String _openid;  // 微信openid（兼容字段）
    private String openid;  // 微信openid
    private String nickname;  // 用户昵称
    private String avatarUrl;  // 用户头像URL
    private String phone;  // 用户手机号
    private Date registerTime;  // 注册时间
}
