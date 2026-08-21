package com.pwbackend.service;

import com.pwbackend.entity.User;
import com.pwbackend.repository.UserRepository;
import com.pwbackend.repository.OpsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务层
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OpsRepository opsRepository;
    private final MongoTemplate mongoTemplate;

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 根据openid获取用户
     */
    public Optional<User> getUserByOpenid(String openid) {
        return userRepository.findByOpenid(openid);
    }

    /**
     * 根据openid获取或创建用户
     */
    public User getOrCreateUser(String openid) {
        Optional<User> userOpt = userRepository.findByOpenid(openid);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        // 创建新用户
        User newUser = new User();
        newUser.setId(openid);
        newUser.setOpenid(openid);
        newUser.setNickname("微信用户");
        newUser.setAvatarUrl("");
        newUser.setRegisterTime(new Date());
        return userRepository.save(newUser);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 根据openid更新用户信息
     * @param openid 用户openid
     * @param nickname 用户昵称
     * @param avatarUrl 用户头像URL
     * @param phone 用户手机号
     * @return 更新后的用户信息
     */
    public User updateUser(String openid, String nickname, String avatarUrl, String phone) {
        Optional<User> userOpt = userRepository.findByOpenid(openid);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (nickname != null && !nickname.isEmpty()) {
                user.setNickname(nickname);
            }
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                user.setAvatarUrl(avatarUrl);
            }
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
            }
            return userRepository.save(user);
        }
        throw new RuntimeException("用户不存在");
    }

    /**
     * 检查用户是否为客服
     * @param openid 用户openid
     * @return true表示是客服，false表示不是客服
     */
    public boolean isOps(String openid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("openid").is(openid));
        return mongoTemplate.count(query, "ops") > 0;
    }

    /**
     * 保存用户
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
