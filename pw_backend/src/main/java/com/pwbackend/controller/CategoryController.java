package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.CategoryList;
import com.pwbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 获取所有分类列表对象
     */
    @GetMapping
    public ApiResponse<List<CategoryList>> getAllCategories() {
        return ApiResponse.success(categoryService.getAllCategoryListObjects());
    }

    /**
     * 根据游戏ID获取分类列表
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Map<String, Object>> getCategoriesByGame(@PathVariable String gameId) {
        List<String> categories = categoryService.getCategoryListByGameId(gameId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", categories);

        return ResponseEntity.ok(result);
    }

    /**
     * 创建/更新分类
     */
    @PostMapping
    public ApiResponse<CategoryList> saveCategory(@RequestBody CategoryList category) {
        return ApiResponse.success(categoryService.saveCategory(category));
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success(null);
    }
}
