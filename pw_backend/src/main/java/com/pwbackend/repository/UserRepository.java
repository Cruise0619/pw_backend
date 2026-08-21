package com.pwbackend.repository;

import com.pwbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * 根据openid查询用户
     */
    @Query("{'openid': ?0}")
    Optional<User> findByOpenid(String openid);
}
