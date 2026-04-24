package com.grid07_bakend.service;

import com.grid07_bakend.dto.CreatePostRequest;
import com.grid07_bakend.model.Post;
import com.grid07_bakend.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final RedisService redisService;


    public Post createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .authorId(request.getAuthorId())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    public void likePost(Long postId) {
        // 🔥 Human Like = +20
        redisService.incrementVirality(postId, 20);
    }
}
