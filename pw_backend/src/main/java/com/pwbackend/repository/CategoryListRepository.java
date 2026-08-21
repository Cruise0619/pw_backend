package com.pwbackend.repository;

import com.pwbackend.entity.CategoryList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 分类列表数据访问层 - MongoDB版本
 */
@Repository
public interface CategoryListRepository extends MongoRepository<CategoryList, String> {

    /**
     * 根据名称查找分类列表
     */
    @Query("{'name': ?0}")
    Optional<CategoryList> findByName(String name);

    /**
     * 根据游戏ID查找分类列表
     */
    @Query("{'gameId': ?0}")
    Optional<CategoryList> findByGameId(String gameId);

    /**
     * 获取所有分类列表
     */
    List<CategoryList> findAll();
}
