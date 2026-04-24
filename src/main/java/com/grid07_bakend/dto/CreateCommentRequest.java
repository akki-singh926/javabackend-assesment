package com.grid07_bakend.dto;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private Long postId;
    private Long authorId;
    private String content;
    private int depthLevel;
}
