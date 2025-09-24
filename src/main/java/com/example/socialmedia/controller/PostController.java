package com.example.socialmedia.controller;

import com.example.socialmedia.dto.PostResponse;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.service.PostService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Post operations.
 * Handles HTTP requests related to post management.
 */
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
@Tag(name = "Post Management", description = "Content creation, retrieval, and management operations")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {
    
    private final PostService postService;
    
    /**
     * Constructor injection for PostService
     * @param postService the post service
     */
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    /**
     * Create a new post
     * @param post the post to create
     * @return ResponseEntity containing the created post
     */
    @Operation(summary = "Create New Post", description = "Create a new post with content and user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Post.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "content": "Just had an amazing day at the beach! 🌊",
                                        "createdAt": "2024-01-15T10:30:00",
                                        "updatedAt": "2024-01-15T10:30:00",
                                        "user": {
                                            "id": 1,
                                            "username": "alice_smith",
                                            "firstName": "Alice",
                                            "lastName": "Smith"
                                        }
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid post data",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "error": "Post content is required"
                                    }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody Post post) {
        try {
            Post createdPost = postService.createPost(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Create a new post for a specific user
     * @param userId the user ID
     * @param content the post content
     * @return ResponseEntity containing the created post
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createPost(@PathVariable Long userId, @RequestBody String content) {
        try {
            Post createdPost = postService.createPost(content, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Get all posts
     * @return ResponseEntity containing list of all posts
     */
    @Operation(summary = "Get All Posts", description = "Retrieve all posts in the system (use pagination for better performance)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }
    
    /**
     * Get all posts with pagination
     * @param page the page number (0-based)
     * @param size the page size
     * @return ResponseEntity containing page of posts
     */
    @Operation(summary = "Get Posts with Pagination", description = "Retrieve posts with pagination support for better performance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated posts retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/paginated")
    public ResponseEntity<Page<Post>> getAllPostsPaginated(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of posts per page", example = "10") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Get a post by ID
     * @param id the post ID
     * @return ResponseEntity containing the post if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            return ResponseEntity.ok(new PostResponse(post.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get all posts by a specific user
     * @param userId the user ID
     * @return ResponseEntity containing list of posts by the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }
    
    /**
     * Get all posts by a specific user with pagination
     * @param userId the user ID
     * @param page the page number (0-based)
     * @param size the page size
     * @return ResponseEntity containing page of posts by the user
     */
    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<Post>> getPostsByUserIdPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.getPostsByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Update an existing post
     * @param id the post ID
     * @param postDetails the updated post details
     * @return ResponseEntity containing the updated post
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody Post postDetails) {
        try {
            Post updatedPost = postService.updatePost(id, postDetails);
            return ResponseEntity.ok(new PostResponse(updatedPost));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Delete a post by ID
     * @param id the post ID
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().body("Post deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Search posts by content
     * @param content the content to search for
     * @return ResponseEntity containing list of matching posts
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPostsByContent(@RequestParam String content) {
        List<Post> posts = postService.searchPostsByContent(content);
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }
    
    /**
     * Search posts by content with pagination
     * @param content the content to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return ResponseEntity containing page of matching posts
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<Post>> searchPostsByContentPaginated(
            @RequestParam String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postService.searchPostsByContent(content, pageable);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * Get posts created after a specific date
     * @param date the date to filter by (ISO format)
     * @return ResponseEntity containing list of posts
     */
    @GetMapping("/after/{date}")
    public ResponseEntity<List<PostResponse>> getPostsAfterDate(@PathVariable String date) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date);
            List<Post> posts = postService.getPostsAfterDate(dateTime);
            List<PostResponse> postResponses = posts.stream()
                    .map(PostResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(postResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * Get posts created between two dates
     * @param startDate the start date (ISO format)
     * @param endDate the end date (ISO format)
     * @return ResponseEntity containing list of posts
     */
    @GetMapping("/between/{startDate}/{endDate}")
    public ResponseEntity<List<PostResponse>> getPostsBetweenDates(
            @PathVariable String startDate,
            @PathVariable String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<Post> posts = postService.getPostsBetweenDates(start, end);
            List<PostResponse> postResponses = posts.stream()
                    .map(PostResponse::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(postResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * Get the count of posts by a specific user
     * @param userId the user ID
     * @return ResponseEntity containing the post count
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getPostCountByUserId(@PathVariable Long userId) {
        long count = postService.getPostCountByUserId(userId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get the feed for a specific user (posts from users they follow)
     * @param userId the user ID
     * @return ResponseEntity containing list of posts from followed users
     */
    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<PostResponse>> getFeedByUserId(@PathVariable Long userId) {
        List<Post> feed = postService.getFeedByUserId(userId);
        List<PostResponse> postResponses = feed.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }
    
    /**
     * Get the feed for a specific user with pagination (posts from users they follow)
     * @param userId the user ID
     * @param page the page number (0-based)
     * @param size the page size
     * @return ResponseEntity containing page of posts from followed users
     */
    @GetMapping("/feed/{userId}/paginated")
    public ResponseEntity<Page<Post>> getFeedByUserIdPaginated(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> feed = postService.getFeedByUserId(userId, pageable);
        return ResponseEntity.ok(feed);
    }
}


