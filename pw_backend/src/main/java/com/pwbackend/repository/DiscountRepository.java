package com.pwbackend.repository;

import com.pwbackend.entity.Discount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 折扣数据访问层 - MongoDB版本
 */
@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {
}
