package com.pwbackend.repository;

import com.pwbackend.entity.ScoreRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 评分记录数据访问层
 */
@Repository
public interface ScoreRecordRepository extends MongoRepository<ScoreRecord, String> {

    /**
     * 根据玩家ID查询评分记录
     */
    @Query("{'playerId': ?0}")
    List<ScoreRecord> findByPlayerId(String playerId);

    /**
     * 根据订单ID查询评分记录
     */
    @Query("{'orderId': ?0}")
    List<ScoreRecord> findByOrderId(String orderId);

    /**
     * 根据用户openid查询评分记录
     */
    @Query("{'openid': ?0}")
    List<ScoreRecord> findByOpenid(String openid);
}
