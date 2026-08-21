package com.pwbackend.repository;

import com.pwbackend.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 商品数据访问层
 */
@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    /**
     * 根据游戏类型查询商品
     */
    @Query("{'game': ?0}")
    List<Item> findByGame(String game);
}
