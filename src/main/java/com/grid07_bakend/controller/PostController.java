package com.grid07_bakend.controller;

import com.grid07_bakend.dto.CreatePostRequest;
import com.grid07_bakend.model.Post;
import com.grid07_bakend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest request){
        return postService.createPost(request);
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return "Post liked";
    }
}
