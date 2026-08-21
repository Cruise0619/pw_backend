package com.pwbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.pwbackend.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 数据导入服务
 * 用于将database目录下的JSON数据导入到MongoDB
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataImportService {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        // 配置 ObjectMapper 忽略未知字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // 数据库目录路径（绝对路径，确保能找到数据文件）
    private static final String DATABASE_DIR = "D:\\pw_backend\\database";

    /**
     * 导入指定集合的数据
     */
    public String importCollections(List<String> collections) {
        try {
            log.info("开始导入数据，选中的集合: {}", collections);
            int totalCount = 0;
            StringBuilder result = new StringBuilder();

            for (String collection : collections) {
                int count = 0;
                switch (collection) {
                    case "users":
                        count = importUsers();
                        break;
                    case "items":
                        count = importItems();
                        break;
                    case "orders":
                        count = importOrders();
                        break;
                    case "ops":
                        count = importOps();
                        break;
                    case "discounts":
                        count = importDiscounts();
                        break;
                    case "players":
                        count = importPlayers();
                        break;
                    case "categories":
                        count = importCategoryList();
                        break;
                    case "scores":
                        count = importScores();
                        break;
                }
                totalCount += count;
                result.append(collection).append(": ").append(count).append(" 条\n");
            }

            log.info("数据导入完成，共导入 {} 条记录", totalCount);
            return "数据导入成功，共导入 " + totalCount + " 条记录\n详情：\n" + result.toString();
        } catch (Exception e) {
            log.error("数据导入失败", e);
            throw new RuntimeException("数据导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入所有数据
     */
    public String importAllData() {
        try {
            log.info("开始导入数据...");
            int totalCount = 0;

            // 导入用户数据
            int userCount = importUsers();
            totalCount += userCount;
            log.info("导入用户数据: {} 条", userCount);

            // 导入商品数据
            int itemCount = importItems();
            totalCount += itemCount;
            log.info("导入商品数据: {} 条", itemCount);

            // 导入订单数据
            int orderCount = importOrders();
            totalCount += orderCount;
            log.info("导入订单数据: {} 条", orderCount);

            // 导入客服数据
            int opsCount = importOps();
            totalCount += opsCount;
            log.info("导入客服数据: {} 条", opsCount);

            // 导入折扣数据
            int discountCount = importDiscounts();
            totalCount += discountCount;
            log.info("导入折扣数据: {} 条", discountCount);

            // 导入陪玩玩家数据
            int playerCount = importPlayers();
            totalCount += playerCount;
            log.info("导入陪玩玩家数据: {} 条", playerCount);

            // 导入分类列表数据
            int categoryCount = importCategoryList();
            totalCount += categoryCount;
            log.info("导入分类列表数据: {} 条", categoryCount);

            // 导入评分记录数据
            int scoreCount = importScores();
            totalCount += scoreCount;
            log.info("导入评分记录数据: {} 条", scoreCount);

            log.info("数据导入完成，共导入 {} 条记录", totalCount);
            return "数据导入成功，共导入 " + totalCount + " 条记录";
        } catch (Exception e) {
            log.error("数据导入失败", e);
            throw new RuntimeException("数据导入失败: " + e.getMessage());
        }
    }

    /**
     * 导入用户数据
     */
    private int importUsers() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_users.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            try {
                // 处理 MongoDB 扩展 JSON 格式
                String processedLine = line;
                // 将 registerTime 的格式从 {"$date":"..."} 转换为 "..."
                if (line.contains("\"registerTime\":{\"$date\":")) {
                    processedLine = line.replace("\"registerTime\":{\"$date\":", "\"registerTime\":");
                    processedLine = processedLine.replace("}\",", "\",");
                    processedLine = processedLine.replace("}\"", "\"");
                }
                User user = objectMapper.readValue(processedLine, User.class);
                users.add(user);
            } catch (Exception e) {
                log.warn("解析用户数据失败: {}, 错误: {}", line, e.getMessage());
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(User.class);
        // 批量插入
        mongoTemplate.insertAll(users);

        return users.size();
    }

    /**
     * 导入商品数据
     * 注意：保留原始JSON中的_id作为MongoDB的_id，确保与图片文件匹配
     */
    private int importItems() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_items.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<Item> items = new ArrayList<>();

        for (String line : lines) {
            try {
                // 使用 JsonNode 手动解析，解决 MongoDB _id -> Java id 字段映射问题
                com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(line);
                Item item = new Item();

                // 将 MongoDB 的 _id 映射到实体类的 id 字段（核心修复）
                if (jsonNode.has("_id") && !jsonNode.get("_id").asText().isEmpty()) {
                    item.setId(jsonNode.get("_id").asText());
                } else {
                    continue; // 没有有效 id 则跳过该记录
                }

                // 逐字段映射其余属性
                if (jsonNode.has("title")) item.setTitle(jsonNode.get("title").asText());
                if (jsonNode.has("describe")) item.setDescribe(jsonNode.get("describe").asText());
                if (jsonNode.has("detail")) item.setDetail(jsonNode.get("detail").asText());
                if (jsonNode.has("price")) item.setPrice(jsonNode.get("price").asText());
                if (jsonNode.has("sales")) item.setSales(jsonNode.get("sales").asText());
                if (jsonNode.has("game")) item.setGame(jsonNode.get("game").asText());

                // 解析 categories 数组字段
                if (jsonNode.has("categories") && jsonNode.get("categories").isArray()) {
                    List<String> categories = new ArrayList<>();
                    for (com.fasterxml.jackson.databind.JsonNode cat : jsonNode.get("categories")) {
                        categories.add(cat.asText());
                    }
                    item.setCategories(categories);
                }

                // 解析 rules 对象字段（购买规则）
                if (jsonNode.has("rules") && jsonNode.get("rules").isObject()) {
                    Map<String, Double> rules = new HashMap<>();
                    jsonNode.get("rules").fields().forEachRemaining(entry ->
                        rules.put(entry.getKey(), entry.getValue().asDouble())
                    );
                    item.setRules(rules);
                }

                items.add(item);
            } catch (Exception e) {
                log.warn("解析商品数据失败: {}", e.getMessage());
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(Item.class);

        // 逐个插入，保留原始 _id 作为 MongoDB 主键
        for (Item item : items) {
            mongoTemplate.save(item);
        }

        log.info("成功导入 {} 个商品", items.size());
        return items.size();
    }

    /**
     * 导入订单数据
     */
    private int importOrders() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_orders.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<Order> orders = new ArrayList<>();

        for (String line : lines) {
            try {
                Order order = objectMapper.readValue(line, Order.class);
                orders.add(order);
            } catch (Exception e) {
                log.warn("解析订单数据失败: {}", line);
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(Order.class);
        // 批量插入
        mongoTemplate.insertAll(orders);

        return orders.size();
    }

    /**
     * 导入客服数据
     */
    private int importOps() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_ops.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<Ops> opsList = new ArrayList<>();

        for (String line : lines) {
            try {
                // 处理 MongoDB 扩展 JSON 格式
                String processedLine = line;
                // 将 registerTime 的格式从 {"$date":"..."} 转换为 "..."
                if (line.contains("\"registerTime\":{\"$date\":")) {
                    processedLine = line.replace("\"registerTime\":{\"$date\":", "\"registerTime\":");
                    processedLine = processedLine.replace("}\",", "\",");
                    processedLine = processedLine.replace("}\"", "\"");
                }
                Ops ops = objectMapper.readValue(processedLine, Ops.class);
                opsList.add(ops);
            } catch (Exception e) {
                log.warn("解析客服数据失败: {}, 错误: {}", line, e.getMessage());
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(Ops.class);
        // 批量插入
        mongoTemplate.insertAll(opsList);

        return opsList.size();
    }

    /**
     * 导入折扣数据
     * 使用JsonNode手动解析解决pay_Percent字段映射问题（Jackson不识别@Field注解）
     */
    private int importDiscounts() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_disconts.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<Discount> discounts = new ArrayList<>();

        for (String line : lines) {
            try {
                // 使用 JsonNode 手动解析
                com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(line);
                Discount discount = new Discount();

                // 映射 _id -> id
                if (jsonNode.has("_id") && !jsonNode.get("_id").asText().isEmpty()) {
                    discount.setId(jsonNode.get("_id").asText());
                }

                // 映射 name
                if (jsonNode.has("name")) {
                    discount.setName(jsonNode.get("name").asText());
                }

                // 映射 active
                if (jsonNode.has("active")) {
                    discount.setActive(jsonNode.get("active").asBoolean());
                }

                // 关键修复：将 JSON 的 pay_Percent 映射到实体的 payPercent 字段
                if (jsonNode.has("pay_Percent")) {
                    discount.setPayPercent(jsonNode.get("pay_Percent").asDouble());
                } else if (jsonNode.has("payPercent")) {
                    discount.setPayPercent(jsonNode.get("payPercent").asDouble());
                }

                discounts.add(discount);
            } catch (Exception e) {
                log.warn("解析折扣数据失败: {}", e.getMessage());
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(Discount.class);
        // 批量插入
        mongoTemplate.insertAll(discounts);

            log.info("成功导入 {} 条折扣数据", discounts.size());
        for (Discount d : discounts) {
            log.info("  折扣: {} | active: {} | payPercent: {}", d.getName(), d.getActive(), d.getPayPercent());
        }
        return discounts.size();
    }

    /**
     * 导入陪玩玩家数据
     * 注意：保留原始JSON中的id作为MongoDB的_id，确保与头像文件匹配
     * 使用TypeReference正确处理嵌套的PlayerGame类型
     */
    private int importPlayers() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_mockPlayers.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<MockPlayer> players = new ArrayList<>();
        com.fasterxml.jackson.core.type.TypeReference<MockPlayer> typeRef = 
            new com.fasterxml.jackson.core.type.TypeReference<MockPlayer>() {};

        for (String line : lines) {
            try {
                MockPlayer player = objectMapper.readValue(line, typeRef);
                // 确保保留原始的id作为MongoDB的_id
                // Spring Data MongoDB默认会生成新_id，需要用原始id
                if (player.getId() != null && !player.getId().isEmpty()) {
                    players.add(player);
                    log.debug("成功解析玩家: {}, games: {}", player.getNickName(), player.getGames());
                }
            } catch (Exception e) {
                log.warn("解析陪玩玩家数据失败: {}, 错误: {}", line, e.getMessage());
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(MockPlayer.class);
        
        // 逐个插入，保留原始_id
        for (MockPlayer player : players) {
            // 使用save方法，如果_id存在则覆盖，否则插入
            mongoTemplate.save(player);
        }

        log.info("成功导入 {} 个陪玩玩家", players.size());
        return players.size();
    }

    /**
     * 导入分类列表数据
     */
    private int importCategoryList() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_categorylist.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<CategoryList> categoryLists = new ArrayList<>();

        for (String line : lines) {
            try {
                CategoryList categoryList = objectMapper.readValue(line, CategoryList.class);
                categoryLists.add(categoryList);
            } catch (Exception e) {
                log.warn("解析分类列表数据失败: {}", line);
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(CategoryList.class);
        // 批量插入
        mongoTemplate.insertAll(categoryLists);

        return categoryLists.size();
    }

    /**
     * 导入评分记录数据
     */
    private int importScores() throws IOException {
        File file = new File(DATABASE_DIR + "\\database_score_rec.json");
        if (!file.exists()) {
            return 0;
        }

        List<String> lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        List<ScoreRecord> scoreRecords = new ArrayList<>();

        for (String line : lines) {
            try {
                ScoreRecord scoreRecord = objectMapper.readValue(line, ScoreRecord.class);
                scoreRecords.add(scoreRecord);
            } catch (Exception e) {
                log.warn("解析评分记录数据失败: {}", line);
            }
        }

        // 清空现有数据
        mongoTemplate.dropCollection(ScoreRecord.class);
        // 批量插入
        mongoTemplate.insertAll(scoreRecords);

        return scoreRecords.size();
    }
}
