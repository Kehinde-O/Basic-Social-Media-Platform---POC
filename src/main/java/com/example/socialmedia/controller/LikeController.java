package com.example.socialmedia.controller;

import com.example.socialmedia.model.Like;
import com.example.socialmedia.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Like operations
 */
@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
@Tag(name = "Like Management", description = "Post like/unlike operations")
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {
    
    @Autowired
    private LikeService likeService;
    
    /**
     * Like a post
     */
    @Operation(summary = "Like a Post", description = "Like a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post liked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Like.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "user": {
                                            "id": 1,
                                            "username": "alice_smith"
                                        },
                                        "post": {
                                            "id": 1,
                                            "content": "Just had an amazing day!"
                                        },
                                        "createdAt": "2024-01-15T10:30:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid like request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "error": "You have already liked this post"
                                    }
                                    """)))
    })
    @PostMapping("/{userId}/like/{postId}")
    public ResponseEntity<?> likePost(
            @Parameter(description = "ID of the user liking the post", example = "1") @PathVariable Long userId,
            @Parameter(description = "ID of the post to like", example = "1") @PathVariable Long postId) {
        try {
            Like like = likeService.likePost(userId, postId);
            return ResponseEntity.status(HttpStatus.CREATED).body(like);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Unlike a post
     */
    @Operation(summary = "Unlike a Post", description = "Remove like from a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post unliked successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "message": "Post unliked successfully"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid unlike request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "error": "You have not liked this post"
                                    }
                                    """)))
    })
    @DeleteMapping("/{userId}/unlike/{postId}")
    public ResponseEntity<?> unlikePost(
            @Parameter(description = "ID of the user unliking the post", example = "1") @PathVariable Long userId,
            @Parameter(description = "ID of the post to unlike", example = "1") @PathVariable Long postId) {
        try {
            likeService.unlikePost(userId, postId);
            return ResponseEntity.ok().body(Map.of("message", "Post unliked successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Toggle like (like if not liked, unlike if liked)
     */
    @Operation(summary = "Toggle Like", description = "Like or unlike a post based on current state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like status toggled successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "liked": true,
                                        "message": "Post liked"
                                    }
                                    """)))
    })
    @PostMapping("/{userId}/toggle/{postId}")
    public ResponseEntity<?> toggleLike(
            @Parameter(description = "ID of the user", example = "1") @PathVariable Long userId,
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            boolean liked = likeService.toggleLike(userId, postId);
            return ResponseEntity.ok().body(Map.of(
                    "liked", liked,
                    "message", liked ? "Post liked" : "Post unliked"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Check if user has liked a post
     */
    @Operation(summary = "Check Like Status", description = "Check if a user has liked a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like status retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "liked": true
                                    }
                                    """)))
    })
    @GetMapping("/{userId}/has-liked/{postId}")
    public ResponseEntity<?> hasUserLikedPost(
            @Parameter(description = "ID of the user", example = "1") @PathVariable Long userId,
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            boolean liked = likeService.hasUserLikedPost(userId, postId);
            return ResponseEntity.ok().body(Map.of("liked", liked));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all likes for a post
     */
    @Operation(summary = "Get Post Likes", description = "Get all likes for a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Likes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Like.class)))
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getLikesForPost(
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            List<Like> likes = likeService.getLikesForPost(postId);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get all likes by a user
     */
    @Operation(summary = "Get User Likes", description = "Get all likes by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User likes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Like.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLikesByUser(
            @Parameter(description = "ID of the user", example = "1") @PathVariable Long userId) {
        try {
            List<Like> likes = likeService.getLikesByUser(userId);
            return ResponseEntity.ok(likes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get like count for a post
     */
    @Operation(summary = "Get Post Like Count", description = "Get the number of likes for a specific post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "postId": 1,
                                        "likeCount": 25
                                    }
                                    """)))
    })
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<?> getLikeCountForPost(
            @Parameter(description = "ID of the post", example = "1") @PathVariable Long postId) {
        try {
            long count = likeService.getLikeCountForPost(postId);
            return ResponseEntity.ok().body(Map.of("postId", postId, "likeCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get like count by a user
     */
    @Operation(summary = "Get User Like Count", description = "Get the number of likes given by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User like count retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "userId": 1,
                                        "likeCount": 50
                                    }
                                    """)))
    })
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> getLikeCountByUser(
            @Parameter(description = "ID of the user", example = "1") @PathVariable Long userId) {
        try {
            long count = likeService.getLikeCountByUser(userId);
            return ResponseEntity.ok().body(Map.of("userId", userId, "likeCount", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get most liked posts
     */
    @Operation(summary = "Get Most Liked Posts", description = "Get posts ordered by like count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Most liked posts retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/most-liked")
    public ResponseEntity<?> getMostLikedPosts() {
        try {
            List<Object[]> mostLiked = likeService.getMostLikedPosts();
            return ResponseEntity.ok(mostLiked);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
