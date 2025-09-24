package com.example.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating a new post
 */
@Schema(description = "Request object for creating a new post")
public class PostRequest {
    
    @NotBlank(message = "Post content is required")
    @Size(max = 1000, message = "Post content must not exceed 1000 characters")
    @Schema(description = "The content of the post", example = "Just had an amazing day at the beach! 🌊", maxLength = 1000)
    private String content;
    
    @Schema(description = "ID of the user creating the post", example = "1")
    private Long userId;
    
    public PostRequest() {}
    
    public PostRequest(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
