package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.Item;
import com.pwbackend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;

    /**
     * 获取所有商品，支持按ID列表筛选
     */
    @GetMapping
    public ApiResponse<List<Item>> getAllItems(@RequestParam(required = false) String ids) {
        if (ids != null && !ids.isEmpty()) {
            List<String> idList = Arrays.asList(ids.split(","));
            return ApiResponse.success(itemService.getItemsByIds(idList));
        }
        return ApiResponse.success(itemService.getAllItems());
    }

    /**
     * 获取随机商品
     */
    @GetMapping("/random")
    public ApiResponse<List<Item>> getRandomItems(@RequestParam(defaultValue = "6") int count) {
        return ApiResponse.success(itemService.getRandomItems(count));
    }

    /**
     * 根据游戏类型获取随机商品
     */
    @GetMapping("/random/{game}")
    public ApiResponse<List<Item>> getRandomItemsByGame(
            @PathVariable String game,
            @RequestParam(defaultValue = "6") int count) {
        return ApiResponse.success(itemService.getRandomItemsByGame(game, count));
    }

    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public ApiResponse<Item> getItemById(@PathVariable String id) {
        return itemService.getItemById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("商品不存在"));
    }

    /**
     * 根据游戏类型获取商品（支持分页）
     */
    @GetMapping("/game/{game}")
    public ApiResponse<List<Item>> getItemsByGame(@PathVariable String game,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "0") int limit) {
        return ApiResponse.success(itemService.getItemsByGame(game, skip, limit));
    }

    /**
     * 根据游戏类型统计商品数量
     */
    @GetMapping("/game/{game}/count")
    public ApiResponse<Long> countItemsByGame(@PathVariable String game) {
        return ApiResponse.success(itemService.countByGame(game));
    }

    /**
     * 根据分类获取商品
     */
    @GetMapping("/category/{category}")
    public ApiResponse<List<Item>> getItemsByCategory(@PathVariable String category) {
        return ApiResponse.success(itemService.getItemsByCategory(category));
    }

    /**
     * 根据游戏类型和分类获取商品（支持分页）
     */
    @GetMapping("/game/{game}/category/{category}")
    public ApiResponse<List<Item>> getItemsByGameAndCategory(
            @PathVariable String game,
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "0") int limit) {
        return ApiResponse.success(itemService.getItemsByGameAndCategory(game, category, skip, limit));
    }

    /**
     * 获取符合条件的商品总数
     */
    @GetMapping("/game/{game}/category/{category}/count")
    public ApiResponse<Long> countItemsByGameAndCategory(
            @PathVariable String game,
            @PathVariable String category) {
        return ApiResponse.success(itemService.countByGameAndCategory(game, category));
    }

    /**
     * 根据游戏类型获取商品（排除指定分类）
     */
    @GetMapping("/game/{game}/exclude")
    public ApiResponse<List<Item>> getItemsByGameExcludingCategories(
            @PathVariable String game,
            @RequestParam List<String> categories) {
        return ApiResponse.success(itemService.getItemsByGameExcludingCategories(game, categories));
    }

    /**
     * 获取所有游戏类型
     */
    @GetMapping("/games")
    public ApiResponse<List<String>> getAllGameTypes() {
        return ApiResponse.success(itemService.getAllGameTypes());
    }

    /**
     * 保存商品
     */
    @PostMapping
    public ApiResponse<Item> saveItem(@RequestBody Item item) {
        return ApiResponse.success(itemService.saveItem(item));
    }

    /**
     * 更新商品
     */
    @PutMapping
    public ApiResponse<Item> updateItem(@RequestBody Item item) {
        return ApiResponse.success(itemService.saveItem(item));
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ApiResponse.success(null);
    }
}
