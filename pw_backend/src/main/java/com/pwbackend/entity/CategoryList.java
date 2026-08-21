package com.pwbackend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * 分类列表实体类
 * 对应前端数据库集合：categorylist
 */
@Data
@Document(collection = "categorylist")
public class CategoryList {

    @Id
    private String id;

    private String name;  // 分类列表名称，如 categoryList1
    private List<String> list;  // 分类项列表
}
