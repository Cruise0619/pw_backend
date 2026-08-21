package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 陪玩玩家实体类
 * 对应前端数据库集合：mockPlayers
 */
@Data
@Document(collection = "mockPlayers")
public class MockPlayer {

    @Id
    private String id;

    private String nickName;  // 玩家昵称
    private String wxid;  // 微信号
    private String authStatus;  // 认证状态：approved
    private List<PlayerGame> games;  // 游戏信息列表
    private String avatarUrl;  // 头像URL，不存储到数据库，由后端动态生成

    /**
     * 玩家游戏信息内部类
     */
    @Data
    public static class PlayerGame {
        private String id;  // 游戏ID：cs2, val, delta, lol
        private String name;  // 游戏名称
        private String platform;  // 平台
        private String rank;  // 段位
        private List<String> categories;  // 支持的分类
        private ScoreInfo scores;  // 评分信息

        @Data
        public static class ScoreInfo {
            private Double score = 0.0;  // 总分
            private Integer count = 0;  // 评分次数
        }
    }
}
