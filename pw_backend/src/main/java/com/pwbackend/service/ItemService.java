package com.pwbackend.service;

import com.pwbackend.entity.Item;
import com.pwbackend.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品服务层
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 获取所有商品
     */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /**
     * 根据ID获取商品
     */
    public Optional<Item> getItemById(String id) {
        return itemRepository.findById(id);
    }

    /**
     * 根据ID列表获取商品
     */
    public List<Item> getItemsByIds(List<String> ids) {
        return mongoTemplate.find(new Query(Criteria.where("_id").in(ids)), Item.class);
    }

    /**
     * 根据游戏类型获取商品（支持分页）
     */
    public List<Item> getItemsByGame(String game, int skip, int limit) {
        Query query = new Query(Criteria.where("game").is(game));
        if (skip > 0) query.skip(skip);
        if (limit > 0) query.limit(limit);
        return mongoTemplate.find(query, Item.class);
    }

    /**
     * 根据游戏类型统计商品数量
     */
    public long countByGame(String game) {
        return mongoTemplate.count(new Query(Criteria.where("game").is(game)), Item.class);
    }

    /**
     * 根据分类获取商品
     */
    public List<Item> getItemsByCategory(String category) {
        Criteria criteria = Criteria.where("categories").in(category);
        return mongoTemplate.find(new Query(criteria), Item.class);
    }

    /**
     * 根据游戏类型和分类获取商品（支持分页）
     */
    public List<Item> getItemsByGameAndCategory(String game, String category, int skip, int limit) {
        Criteria criteria = Criteria.where("game").is(game);
        criteria.and("categories").in(category);
        Query query = new Query(criteria);
        if (skip > 0) query.skip(skip);
        if (limit > 0) query.limit(limit);
        return mongoTemplate.find(query, Item.class);
    }

    /**
     * 根据游戏类型和分类统计商品数量
     */
    public long countByGameAndCategory(String game, String category) {
        Criteria criteria = Criteria.where("game").is(game);
        criteria.and("categories").in(category);
        return mongoTemplate.count(new Query(criteria), Item.class);
    }

    /**
     * 根据游戏类型获取商品（排除指定分类）
     */
    public List<Item> getItemsByGameExcludingCategories(String game, List<String> excludeCategories) {
        Criteria criteria = Criteria.where("game").is(game);
        criteria.and("categories").nin(excludeCategories);
        return mongoTemplate.find(new Query(criteria), Item.class);
    }

    /**
     * 随机获取商品
     */
    public List<Item> getRandomItems(int count) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.sample(count)
        );
        return mongoTemplate.aggregate(aggregation, "items", Item.class).getMappedResults();
    }

    /**
     * 根据游戏随机获取商品
     */
    public List<Item> getRandomItemsByGame(String game, int count) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("game").is(game)),
            Aggregation.sample(count)
        );
        return mongoTemplate.aggregate(aggregation, "items", Item.class).getMappedResults();
    }

    /**
     * 获取所有游戏类型
     */
    public List<String> getAllGameTypes() {
        List<Item> items = itemRepository.findAll();
        List<String> games = new ArrayList<>();
        for (Item item : items) {
            if (item.getGame() != null && !games.contains(item.getGame())) {
                games.add(item.getGame());
            }
        }
        return games;
    }

    /**
     * 保存商品
     */
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * 删除商品
     */
    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }
}
