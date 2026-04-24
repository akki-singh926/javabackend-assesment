package com.grid07_bakend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final StringRedisTemplate redisTemplate;

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void processNotifications() {

        ScanOptions options = ScanOptions.scanOptions()
                .match("user:*:pending_notifs")
                .count(100)
                .build();

        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                .getConnection()
                .scan(options)) {

            while (cursor.hasNext()) {

                String key = new String(cursor.next(), StandardCharsets.UTF_8);

                Long size = redisTemplate.opsForList().size(key);

                if (size != null && size > 0) {

                    String firstMessage = redisTemplate.opsForList().leftPop(key);

                    System.out.println(
                            "Summarized Push Notification: "
                                    + firstMessage
                                    + " and " + (size - 1) + " others interacted."
                    );

                    redisTemplate.delete(key);
                }
            }
        }
    }
}
