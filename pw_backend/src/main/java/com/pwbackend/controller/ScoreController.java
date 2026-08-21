package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.ScoreRecord;
import com.pwbackend.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 评分控制器
 */
@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * 获取所有评分记录
     */
    @GetMapping
    public ApiResponse<List<ScoreRecord>> getAllScores() {
        return ApiResponse.success(scoreService.getAllScores());
    }

    /**
     * 根据ID获取评分记录
     */
    @GetMapping("/{id}")
    public ApiResponse<ScoreRecord> getScoreById(@PathVariable String id) {
        return scoreService.getScoreById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("评分记录不存在"));
    }

    /**
     * 根据玩家ID获取评分记录
     */
    @GetMapping("/player/{playerId}")
    public ApiResponse<List<ScoreRecord>> getScoresByPlayerId(@PathVariable String playerId) {
        return ApiResponse.success(scoreService.getScoresByPlayerId(playerId));
    }

    /**
     * 根据订单ID获取评分记录
     */
    @GetMapping("/order/{orderId}")
    public ApiResponse<List<ScoreRecord>> getScoresByOrderId(@PathVariable String orderId) {
        return ApiResponse.success(scoreService.getScoresByOrderId(orderId));
    }

    /**
     * 根据玩家ID和游戏类型获取评分记录
     */
    @GetMapping("/player/{playerId}/game/{gameType}")
    public ApiResponse<List<ScoreRecord>> getScoresByPlayerIdAndGameType(
            @PathVariable String playerId,
            @PathVariable String gameType) {
        return ApiResponse.success(scoreService.getScoresByPlayerIdAndGameType(playerId, gameType));
    }

    /**
     * 提交评分
     */
    @PostMapping
    public ApiResponse<ScoreRecord> submitScore(@RequestBody ScoreRecord scoreRecord) {
        return ApiResponse.success(scoreService.submitScore(scoreRecord));
    }

    /**
     * 删除评分记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteScore(@PathVariable String id) {
        scoreService.deleteScore(id);
        return ApiResponse.success(null);
    }
}
