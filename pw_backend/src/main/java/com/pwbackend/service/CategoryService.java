package com.pwbackend.service;

import com.pwbackend.entity.CategoryList;
import com.pwbackend.repository.CategoryListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 分类服务层 - MongoDB版本
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryListRepository categoryListRepository;

    // 游戏ID映射
    private static final Map<String, String> GAME_ID_MAP = new HashMap<>();
    static {
        GAME_ID_MAP.put("1", "cs2");
        GAME_ID_MAP.put("2", "val");
        GAME_ID_MAP.put("3", "delta");
        GAME_ID_MAP.put("4", "lol");
    }

    /**
     * 获取所有分类列表对象
     */
    public List<CategoryList> getAllCategoryListObjects() {
        return categoryListRepository.findAll();
    }

    /**
     * 获取所有分类列表
     */
    public Map<String, List<String>> getAllCategoryLists() {
        List<CategoryList> categoryLists = categoryListRepository.findAll();
        Map<String, List<String>> result = new HashMap<>();

        for (CategoryList categoryList : categoryLists) {
            String name = categoryList.getName();
            // 从名称中提取游戏ID，如categoryList1 -> 1 -> cs2
            String gameId = GAME_ID_MAP.get(name.replace("categoryList", ""));

            if (gameId != null && categoryList.getList() != null) {
                result.put(gameId, categoryList.getList());
            }
        }

        return result;
    }

    /**
     * 根据游戏ID获取分类列表
     */
    public List<String> getCategoryListByGameId(String gameId) {
        // 反向查找：从游戏ID获取对应的分类名称
        String categoryName = null;
        for (Map.Entry<String, String> entry : GAME_ID_MAP.entrySet()) {
            if (entry.getValue().equals(gameId)) {
                categoryName = "categoryList" + entry.getKey();
                break;
            }
        }

        if (categoryName == null) {
            return new ArrayList<>();
        }

        return categoryListRepository.findByName(categoryName)
            .map(CategoryList::getList)
            .orElse(new ArrayList<>());
    }

    /**
     * 保存分类列表
     */
    public CategoryList saveCategory(CategoryList categoryList) {
        return categoryListRepository.save(categoryList);
    }

    /**
     * 保存分类列表
     */
    public CategoryList saveCategoryList(CategoryList categoryList) {
        return categoryListRepository.save(categoryList);
    }

    /**
     * 删除分类列表
     */
    public void deleteCategory(String id) {
        categoryListRepository.deleteById(id);
    }

    /**
     * 初始化默认分类数据
     */
    public void initDefaultCategories() {
        if (categoryListRepository.count() == 0) {
            // CS2分类
            CategoryList cs2 = new CategoryList();
            cs2.setName("categoryList1");
            cs2.setList(Arrays.asList("技术陪", "娱乐陪", "上分大神"));
            categoryListRepository.save(cs2);

            // 无畏契约分类
            CategoryList val = new CategoryList();
            val.setName("categoryList2");
            val.setList(Arrays.asList("技术陪", "娱乐陪"));
            categoryListRepository.save(val);

            // 三角洲行动分类
            CategoryList delta = new CategoryList();
            delta.setName("categoryList3");
            delta.setList(Arrays.asList("技术陪", "娱乐陪"));
            categoryListRepository.save(delta);

            // 英雄联盟分类
            CategoryList lol = new CategoryList();
            lol.setName("categoryList4");
            lol.setList(Arrays.asList("技术陪", "娱乐陪"));
            categoryListRepository.save(lol);
        }
    }
}
