package com.example.socialmedia.controller;

import com.example.socialmedia.model.Comment;
import com.example.socialmedia.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Comment operations
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@Tag(name = "Comment Management", description = "Post comment operations")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * Create a new comment
     */
    @Operation(summary = "Create Comment", description = "Add a comment to a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "content": "Great post! Thanks for sharing.",
                                        "user": {
                                            "id": 1,
                                            "username": "alice_smith"
                                        },
                                        "post": {
                                            "id": 1,
                                            "content": "Just had an amazing day!"
                                        },
                                        "createdAt": "2024-01-15T10:30:00",
                                        "updatedAt": "2024-01-15T10:30:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid comment data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "error": "Comment content is required"
                                    }
                                    """)))
    })
    @PostMapping("/{userId}/comment/{postId}")
    public ResponseEntity<?> createComment(
            @Parameter(description = "ID of the user creating the comment", example = "1") @PathVariable Long userId,
            @Parameter(description = "ID of the post to comment on", example = "1") @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest) {
        try {
            Comment comment = commentService.createComment(userId, postId, commentRequest.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all comments for a post
     */
    @Operation(summary = "Get Post Comments", description = "Get all comments for a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsForPost(
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsForPost(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all comments for a post with pagination
     */
    @Operation(summary = "Get Post Comments (Paginated)", description = "Get comments for a post with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/post/{postId}/paginated")
    public ResponseEntity<?> getCommentsForPostPaginated(
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId,
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of comments per page", example = "10") @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Comment> comments = commentService.getCommentsForPost(postId, pageable);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all comments by a user
     */
    @Operation(summary = "Get User Comments", description = "Get all comments made by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentsByUser(
            @Parameter(description = "ID of the user", example = "1") @PathVariable Long userId) {
        try {
            List<Comment> comments = commentService.getCommentsByUser(userId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get comment by ID
     */
    @Operation(summary = "Get Comment by ID", description = "Get a specific comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(
            @Parameter(description = "ID of the comment", example = "1") @PathVariable Long commentId) {
        try {
            Comment comment = commentService.getCommentById(commentId);
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Update a comment
     */
    @Operation(summary = "Update Comment", description = "Update the content of a specific comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @Parameter(description = "ID of the comment to update", example = "1") @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest commentRequest) {
        try {
            Comment comment = commentService.updateComment(commentId, commentRequest.getContent());
            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Delete a comment
     */
    @Operation(summary = "Delete Comment", description = "Delete a specific comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "message": "Comment deleted successfully"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @Parameter(description = "ID of the comment to delete", example = "1") @PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok().body(Map.of("message", "Comment deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get comment count for a post
     */
    @Operation(summary = "Get Post Comment Count", description = "Get the number of comments for a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment count retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "postId": 1,
                                        "commentCount": 8
                                    }
                                    """)))
    })
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getCommentCountForPost(
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            long count = commentService.getCommentCountForPost(postId);
            return ResponseEntity.ok().body(Map.of("postId", postId, "commentCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Search comments by content
     */
    @Operation(summary = "Search Comments", description = "Search comments by content text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchCommentsByContent(
            @Parameter(description = "Search text", example = "amazing") @RequestParam String text) {
        try {
            List<Comment> comments = commentService.searchCommentsByContent(text);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get recent comments across all posts
     */
    @Operation(summary = "Get Recent Comments", description = "Get recent comments across all posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recent comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentComments() {
        try {
            List<Comment> comments = commentService.getRecentComments();
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Comment request DTO
     */
    public static class CommentRequest {
        @NotBlank(message = "Comment content is required")
        @Size(max = 500, message = "Comment content must not exceed 500 characters")
        private String content;
        
        public CommentRequest() {}
        
        public CommentRequest(String content) {
            this.content = content;
        }
        
        public String getContent() {
            return content;
        }
        
        public void setContent(String content) {
            this.content = content;
        }
    }
}
