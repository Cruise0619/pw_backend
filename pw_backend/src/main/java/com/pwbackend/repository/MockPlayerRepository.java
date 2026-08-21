package com.pwbackend.repository;

import com.pwbackend.entity.MockPlayer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 陪玩玩家数据访问层
 */
@Repository
public interface MockPlayerRepository extends MongoRepository<MockPlayer, String> {

    /**
     * 根据认证状态查询玩家
     */
    @Query("{'authStatus': ?0}")
    List<MockPlayer> findByAuthStatus(String authStatus);
}
