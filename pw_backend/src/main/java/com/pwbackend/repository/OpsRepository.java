package com.pwbackend.repository;

import com.pwbackend.entity.Ops;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 客服数据访问层 - MongoDB版本
 */
@Repository
public interface OpsRepository extends MongoRepository<Ops, String> {
}
