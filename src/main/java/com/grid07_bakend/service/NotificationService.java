package com.grid07_bakend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RedisService redisService;

    public void handleBotInteraction(Long userId, String message){

        // check cooldown
        if(redisService.isUserNotificationOnCooldown(userId)){

            // store in redis list
            redisService.pushNotification(userId,message);

        }else{
            // send notification quickly
            System.out.println("Push Notification Sent to User: " + message);

            // set cooldown
            redisService.setUserNotificationCooldown(userId);
        }
    }
}
