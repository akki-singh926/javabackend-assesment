package com.grid07_bakend.dto;

import lombok.Data;

@Data
public class CreatePostRequest {
    private Long authorId;
    private String content;
}
