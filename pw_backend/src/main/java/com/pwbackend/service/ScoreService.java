package com.pwbackend.service;

import com.pwbackend.entity.ScoreRecord;
import com.pwbackend.repository.ScoreRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 评分服务层
 */
@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRecordRepository scoreRecordRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 获取所有评分记录
     */
    public List<ScoreRecord> getAllScores() {
        return scoreRecordRepository.findAll();
    }

    /**
     * 根据ID获取评分记录
     */
    public Optional<ScoreRecord> getScoreById(String id) {
        return scoreRecordRepository.findById(id);
    }

    /**
     * 根据玩家ID获取评分记录
     */
    public List<ScoreRecord> getScoresByPlayerId(String playerId) {
        return scoreRecordRepository.findByPlayerId(playerId);
    }

    /**
     * 根据订单ID获取评分记录
     */
    public List<ScoreRecord> getScoresByOrderId(String orderId) {
        return scoreRecordRepository.findByOrderId(orderId);
    }

    /**
     * 根据玩家ID和游戏类型获取评分记录
     */
    public List<ScoreRecord> getScoresByPlayerIdAndGameType(String playerId, String gameType) {
        Criteria criteria = Criteria.where("playerId").is(playerId);
        criteria.and("gameType").is(gameType);
        return mongoTemplate.find(new Query(criteria), ScoreRecord.class);
    }

    /**
     * 提交评分
     */
    public ScoreRecord submitScore(ScoreRecord scoreRecord) {
        scoreRecord.setCreateTime(new Date());
        return scoreRecordRepository.save(scoreRecord);
    }

    /**
     * 删除评分记录
     */
    public void deleteScore(String id) {
        scoreRecordRepository.deleteById(id);
    }
}
