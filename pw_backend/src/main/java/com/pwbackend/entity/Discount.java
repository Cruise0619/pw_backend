package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 折扣实体类
 * 对应前端数据库集合：disconts
 */
@Data
@Document(collection = "disconts")
public class Discount {

    @Id
    private String id;

    private String name;  // 折扣名称

    private Boolean active;  // 是否生效

    @Field("pay_Percent")
    private Double payPercent;  // 支付比例，如0.8表示8折
}
