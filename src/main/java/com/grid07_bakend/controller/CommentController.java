package com.grid07_bakend.controller;

import com.grid07_bakend.dto.CreateCommentRequest;
import com.grid07_bakend.model.Comment;
import com.grid07_bakend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/posts")
public class CommentController {

    public final CommentService commentService;

    @PostMapping("{postId}/comments")
    public Comment addComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request){

        request.setPostId(postId);
        return commentService.addComment(request);
    }

}
