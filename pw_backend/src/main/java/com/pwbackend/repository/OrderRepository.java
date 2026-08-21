package com.pwbackend.repository;

import com.pwbackend.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 订单数据访问层
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    /**
     * 根据用户openid查询订单
     */
    @Query("{'openid': ?0}")
    List<Order> findByOpenid(String openid);

    /**
     * 根据状态查询订单
     */
    @Query("{'status': ?0}")
    List<Order> findByStatus(String status);

    /**
     * 根据玩家ID查询相关订单
     */
    @Query("{'players': {$in: [?0]}}")
    List<Order> findByPlayersContaining(String playerId);
}
