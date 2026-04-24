package com.grid07_bakend.service;

import com.grid07_bakend.dto.CreateCommentRequest;
import com.grid07_bakend.model.Comment;
import com.grid07_bakend.repo.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final RedisService redisService;

    private final NotificationService notificationService;

    public Comment addComment(CreateCommentRequest request) {

        // Vertical cap
        if (request.getDepthLevel() > 20) {
            throw new RuntimeException("Max depth exceeded");
        }

        // Assume authorId > 1000 = bot (simple logic for now)
        boolean isBot = request.getAuthorId() > 1000;

        if (isBot) {

            Long botId = request.getAuthorId();
            Long humanId = 1L; // TODO: replace with actual human (post owner later)

            //  2. Cooldown Cap (10 min)
            if (redisService.isCooldownActive(botId, humanId)) {
                throw new RuntimeException("Cooldown active (429)");
            }

            redisService.setCooldown(botId, humanId);

            // 3. Horizontal Cap (max 100 bot replies per post)
            Long count = redisService.incrementBotCount(request.getPostId());

            if (count != null && count > 100) {
                // 🔥 rollback increment
                redisService.decrementBotCount(request.getPostId());

                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Bot limit exceeded"
                );
            }


            // Bot reply = +1
            redisService.incrementVirality(request.getPostId(), 1);

            // Notification logic
            Long userId = 1L; // for now assume post owner

            notificationService.handleBotInteraction(
                    userId,
                    "Bot " + botId + " replied to your post"
            );
        } else {
            // Human comment = +50
            redisService.incrementVirality(request.getPostId(), 50);
        }

        Comment comment = Comment.builder()
                .postId(request.getPostId())
                .authorId(request.getAuthorId())
                .content(request.getContent())
                .depthLevel(request.getDepthLevel())
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }
}
