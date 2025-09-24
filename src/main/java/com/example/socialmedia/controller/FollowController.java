package com.example.socialmedia.controller;

import com.example.socialmedia.model.Follow;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.FollowService;
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
import java.util.Optional;

/**
 * REST Controller for Follow operations.
 * Handles HTTP requests related to follow relationship management.
 */
@RestController
@RequestMapping("/api/follows")
@CrossOrigin(origins = "*")
@Tag(name = "Follow Management", description = "User follow relationships and social connections")
@SecurityRequirement(name = "Bearer Authentication")
public class FollowController {
    
    private final FollowService followService;
    
    /**
     * Constructor injection for FollowService
     * @param followService the follow service
     */
    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }
    
    /**
     * Follow a user
     * @param followerId the ID of the user who wants to follow
     * @param followingId the ID of the user to be followed
     * @return ResponseEntity containing the created follow relationship
     */
    @Operation(summary = "Follow User", description = "Create a follow relationship between two users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Follow relationship created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Follow.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "follower": {
                                            "id": 1,
                                            "username": "alice_smith"
                                        },
                                        "following": {
                                            "id": 2,
                                            "username": "bob_jones"
                                        },
                                        "createdAt": "2024-01-15T10:30:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid follow request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "error": "Cannot follow yourself"
                                    }
                                    """)))
    })
    @PostMapping("/{followerId}/follow/{followingId}")
    public ResponseEntity<?> followUser(
            @Parameter(description = "ID of the user who wants to follow", example = "1") @PathVariable Long followerId, 
            @Parameter(description = "ID of the user to be followed", example = "2") @PathVariable Long followingId) {
        try {
            Follow follow = followService.followUser(followerId, followingId);
            return ResponseEntity.status(HttpStatus.CREATED).body(follow);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Unfollow a user
     * @param followerId the ID of the user who wants to unfollow
     * @param followingId the ID of the user to be unfollowed
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("/{followerId}/unfollow/{followingId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        try {
            followService.unfollowUser(followerId, followingId);
            return ResponseEntity.ok().body("Successfully unfollowed user");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Check if a user is following another user
     * @param followerId the ID of the potential follower
     * @param followingId the ID of the potential user being followed
     * @return ResponseEntity containing boolean result
     */
    @GetMapping("/{followerId}/following/{followingId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long followerId, @PathVariable Long followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        return ResponseEntity.ok(isFollowing);
    }
    
    /**
     * Get all users that a specific user is following
     * @param userId the user ID
     * @return ResponseEntity containing list of users being followed
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowing(@PathVariable Long userId) {
        List<User> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }
    
    /**
     * Get all users that follow a specific user
     * @param userId the user ID
     * @return ResponseEntity containing list of followers
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<User>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }
    
    /**
     * Get all follow relationships where a user is the follower
     * @param userId the user ID
     * @return ResponseEntity containing list of follow relationships
     */
    @GetMapping("/{userId}/following/relationships")
    public ResponseEntity<List<Follow>> getFollowingRelationships(@PathVariable Long userId) {
        List<Follow> relationships = followService.getFollowingRelationships(userId);
        return ResponseEntity.ok(relationships);
    }
    
    /**
     * Get all follow relationships where a user is being followed
     * @param userId the user ID
     * @return ResponseEntity containing list of follow relationships
     */
    @GetMapping("/{userId}/followers/relationships")
    public ResponseEntity<List<Follow>> getFollowerRelationships(@PathVariable Long userId) {
        List<Follow> relationships = followService.getFollowerRelationships(userId);
        return ResponseEntity.ok(relationships);
    }
    
    /**
     * Get the count of users that a specific user is following
     * @param userId the user ID
     * @return ResponseEntity containing the following count
     */
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long userId) {
        long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get the count of users that follow a specific user
     * @param userId the user ID
     * @return ResponseEntity containing the followers count
     */
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long userId) {
        long count = followService.getFollowersCount(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get mutual follows between two users (users that both follow)
     * @param userId1 the ID of the first user
     * @param userId2 the ID of the second user
     * @return ResponseEntity containing list of mutual follows
     */
    @GetMapping("/{userId1}/mutual/{userId2}")
    public ResponseEntity<List<User>> getMutualFollows(@PathVariable Long userId1, @PathVariable Long userId2) {
        List<User> mutualFollows = followService.getMutualFollows(userId1, userId2);
        return ResponseEntity.ok(mutualFollows);
    }
    
    /**
     * Get suggested users to follow (users followed by users that the current user follows)
     * @param userId the ID of the current user
     * @return ResponseEntity containing list of suggested users
     */
    @GetMapping("/{userId}/suggestions")
    public ResponseEntity<List<User>> getSuggestedUsers(@PathVariable Long userId) {
        List<User> suggestions = followService.getSuggestedUsers(userId);
        return ResponseEntity.ok(suggestions);
    }
    
    /**
     * Get a specific follow relationship
     * @param followerId the ID of the follower
     * @param followingId the ID of the user being followed
     * @return ResponseEntity containing the follow relationship if it exists
     */
    @GetMapping("/{followerId}/relationship/{followingId}")
    public ResponseEntity<?> getFollowRelationship(@PathVariable Long followerId, @PathVariable Long followingId) {
        Optional<Follow> relationship = followService.getFollowRelationship(followerId, followingId);
        if (relationship.isPresent()) {
            return ResponseEntity.ok(relationship.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


