package com.pwbackend.service;

import com.pwbackend.entity.MockPlayer;
import com.pwbackend.repository.MockPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 陪玩玩家服务层
 */
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final MockPlayerRepository playerRepository;
    private final MongoTemplate mongoTemplate;

    @Value("${image.base-url:http://localhost:8081/storage}")
    private String imageBaseUrl;

    /**
     * 获取所有陪玩玩家
     */
    public List<MockPlayer> getAllPlayers() {
        List<MockPlayer> players = playerRepository.findAll();
        players.forEach(this::setAvatarUrl);
        return players;
    }

    /**
     * 获取已认证的陪玩玩家
     */
    public List<MockPlayer> getApprovedPlayers() {
        List<MockPlayer> players = playerRepository.findByAuthStatus("approved");
        players.forEach(this::setAvatarUrl);
        return players;
    }

    /**
     * 根据ID获取玩家
     */
    public Optional<MockPlayer> getPlayerById(String id) {
        return playerRepository.findById(id);
    }

    /**
     * 根据ID列表获取玩家
     */
    public List<MockPlayer> getPlayersByIds(List<String> ids) {
        List<MockPlayer> players = mongoTemplate.find(
            new Query(Criteria.where("_id").in(ids)), MockPlayer.class);
        players.forEach(this::setAvatarUrl);
        return players;
    }

    /**
     * 根据游戏ID获取玩家
     */
    public List<MockPlayer> getPlayersByGameId(String gameId) {
        Criteria criteria = Criteria.where("games.id").is(gameId);
        List<MockPlayer> players = mongoTemplate.find(new Query(criteria), MockPlayer.class);
        players.forEach(this::setAvatarUrl);
        return players;
    }

    /**
     * 根据游戏ID获取已认证的玩家
     */
    public List<MockPlayer> getApprovedPlayersByGameId(String gameId) {
        Criteria criteria = Criteria.where("games.id").is(gameId);
        criteria.and("authStatus").is("approved");
        List<MockPlayer> players = mongoTemplate.find(new Query(criteria), MockPlayer.class);
        players.forEach(this::setAvatarUrl);
        return players;
    }

    /**
     * 根据游戏ID和分类获取玩家
     */
    public List<MockPlayer> getPlayersByGameIdAndCategory(String gameId, String category) {
        List<MockPlayer> players = getApprovedPlayersByGameId(gameId);
        players.removeIf(player -> {
            if (player.getGames() == null) return true;
            return player.getGames().stream()
                .noneMatch(game -> game.getCategories() != null && game.getCategories().contains(category));
        });
        return players;
    }

    /**
     * 保存玩家
     */
    public MockPlayer savePlayer(MockPlayer player) {
        return playerRepository.save(player);
    }

    /**
     * 删除玩家
     */
    public void deletePlayer(String id) {
        playerRepository.deleteById(id);
    }

    /**
     * 设置玩家头像URL
     * 头像使用本地存储路径，格式：http://localhost:8081/storage/avatar/{id}.jpg
     */
    private void setAvatarUrl(MockPlayer player) {
        if (player != null && player.getId() != null) {
            player.setAvatarUrl(imageBaseUrl + "/avatar/" + player.getId() + ".jpg");
        }
    }
}
