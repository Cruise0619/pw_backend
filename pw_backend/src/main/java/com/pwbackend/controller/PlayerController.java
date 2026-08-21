package com.pwbackend.controller;

import com.pwbackend.dto.ApiResponse;
import com.pwbackend.entity.MockPlayer;
import com.pwbackend.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * 陪玩玩家控制器
 */
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PlayerController {

    private final PlayerService playerService;

    /**
     * 获取所有陪玩玩家，支持按ID列表筛选
     */
    @GetMapping
    public ApiResponse<List<MockPlayer>> getAllPlayers(@RequestParam(required = false) String ids) {
        if (ids != null && !ids.isEmpty()) {
            List<String> idList = Arrays.asList(ids.split(","));
            return ApiResponse.success(playerService.getPlayersByIds(idList));
        }
        return ApiResponse.success(playerService.getAllPlayers());
    }

    /**
     * 获取已认证的陪玩玩家
     */
    @GetMapping("/approved")
    public ApiResponse<List<MockPlayer>> getApprovedPlayers() {
        return ApiResponse.success(playerService.getApprovedPlayers());
    }

    /**
     * 根据ID获取玩家
     */
    @GetMapping("/{id}")
    public ApiResponse<MockPlayer> getPlayerById(@PathVariable String id) {
        return playerService.getPlayerById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("玩家不存在"));
    }

    /**
     * 根据游戏ID获取已认证玩家
     */
    @GetMapping("/game/{gameId}")
    public ApiResponse<List<MockPlayer>> getPlayersByGameId(@PathVariable String gameId) {
        return ApiResponse.success(playerService.getApprovedPlayersByGameId(gameId));
    }

    /**
     * 根据游戏ID和分类获取玩家
     */
    @GetMapping("/game/{gameId}/category/{category}")
    public ApiResponse<List<MockPlayer>> getPlayersByGameIdAndCategory(
            @PathVariable String gameId,
            @PathVariable String category) {
        return ApiResponse.success(playerService.getPlayersByGameIdAndCategory(gameId, category));
    }

    /**
     * 保存玩家
     */
    @PostMapping
    public ApiResponse<MockPlayer> savePlayer(@RequestBody MockPlayer player) {
        return ApiResponse.success(playerService.savePlayer(player));
    }

    /**
     * 更新玩家
     */
    @PutMapping("/{id}")
    public ApiResponse<MockPlayer> updatePlayer(@PathVariable String id, @RequestBody MockPlayer player) {
        player.setId(id);
        return ApiResponse.success(playerService.savePlayer(player));
    }

    /**
     * 删除玩家
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePlayer(@PathVariable String id) {
        playerService.deletePlayer(id);
        return ApiResponse.success(null);
    }
}
