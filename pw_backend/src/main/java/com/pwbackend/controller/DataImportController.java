package com.pwbackend.controller;

import com.pwbackend.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据导入控制器
 * 用于导入database目录下的JSON数据到MongoDB
 */
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DataImportController {

    private final DataImportService dataImportService;

    /**
     * 导入指定集合的数据
     * 警告：此操作会清空指定集合的现有数据！
     */
    @PostMapping("/collections")
    public ResponseEntity<Map<String, Object>> importCollections(@RequestBody Map<String, List<String>> request) {
        Map<String, Object> result = new HashMap<>();

        try {
            List<String> collections = request.get("collections");
            if (collections == null || collections.isEmpty()) {
                result.put("success", false);
                result.put("message", "请指定要导入的集合");
                return ResponseEntity.badRequest().body(result);
            }

            String message = dataImportService.importCollections(collections);
            result.put("success", true);
            result.put("message", message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 导入所有数据
     * 警告：此操作会清空现有数据！
     */
    @PostMapping("/all")
    public ResponseEntity<Map<String, Object>> importAllData() {
        Map<String, Object> result = new HashMap<>();

        try {
            String message = dataImportService.importAllData();
            result.put("success", true);
            result.put("message", message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "数据导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 检查数据导入状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkImportStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "数据导入服务就绪，可以通过 POST /api/import/all 触发数据导入");
        result.put("warning", "警告：数据导入会清空现有数据，请谨慎操作！");
        return ResponseEntity.ok(result);
    }
}
