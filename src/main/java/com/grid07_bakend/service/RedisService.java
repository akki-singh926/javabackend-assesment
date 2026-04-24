package com.grid07_bakend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // 🔥 Increment virality score
    public void incrementVirality(Long postId, int score) {
        String key = "post:" + postId + ":virality_score";
        redisTemplate.opsForValue().increment(key, score);
    }

    public Long incrementBotCount(Long postId) {
        String key = "post:" + postId + ":bot_count";
        return redisTemplate.opsForValue().increment(key);
    }

    public Long decrementBotCount(Long postId) {
        String key = "post:" + postId + ":bot_count";
        return redisTemplate.opsForValue().decrement(key);
    }

    public boolean isCooldownActive(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        redisTemplate.opsForValue().set(key, "1", 10, java.util.concurrent.TimeUnit.MINUTES);
    }

    // Check if user recently notified
    public boolean isUserNotificationOnCooldown(Long userId) {
        String key = "user:" + userId + ":notif_cooldown";
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // Set cooldown (15 min)
    public void setUserNotificationCooldown(Long userId) {
        String key = "user:" + userId + ":notif_cooldown";
        redisTemplate.opsForValue().set(key, "1", 15, java.util.concurrent.TimeUnit.MINUTES);
    }

    // Add notification to list
    public void pushNotification(Long userId, String message) {
        String key = "user:" + userId + ":pending_notifs";
        redisTemplate.opsForList().rightPush(key, message);
    }
}
