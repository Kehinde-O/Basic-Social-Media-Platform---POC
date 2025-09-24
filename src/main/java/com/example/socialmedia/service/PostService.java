package com.example.socialmedia.service;

import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Post entity operations.
 * Contains business logic for post management.
 */
@Service
@Transactional
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    /**
     * Constructor injection for repositories
     * @param postRepository the post repository
     * @param userRepository the user repository
     */
    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Create a new post
     * @param post the post to create
     * @return the created post
     * @throws IllegalArgumentException if user not found
     */
    public Post createPost(Post post) {
        // Validate that the user exists
        if (post.getUser() == null || post.getUser().getId() == null) {
            throw new IllegalArgumentException("User is required for creating a post");
        }
        
        User user = userRepository.findById(post.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + post.getUser().getId()));
        
        post.setUser(user);
        return postRepository.save(post);
    }
    
    /**
     * Create a new post for a specific user
     * @param content the post content
     * @param userId the user ID
     * @return the created post
     * @throws IllegalArgumentException if user not found
     */
    public Post createPost(String content, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        
        Post post = new Post(content, user);
        return postRepository.save(post);
    }
    
    /**
     * Get all posts ordered by creation date (newest first)
     * @return list of all posts
     */
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    /**
     * Get all posts with pagination, ordered by creation date (newest first)
     * @param pageable pagination information
     * @return page of posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    /**
     * Get a post by ID
     * @param id the post ID
     * @return Optional containing the post if found
     */
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    
    /**
     * Get all posts by a specific user
     * @param userId the user ID
     * @return list of posts created by the user
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * Get all posts by a specific user with pagination
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts created by the user
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    /**
     * Get all posts by a specific user
     * @param user the user
     * @return list of posts created by the user
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    /**
     * Get all posts by a specific user with pagination
     * @param user the user
     * @param pageable pagination information
     * @return page of posts created by the user
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    /**
     * Update an existing post
     * @param id the post ID
     * @param postDetails the updated post details
     * @return the updated post
     * @throws IllegalArgumentException if post not found
     */
    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        
        post.setContent(postDetails.getContent());
        return postRepository.save(post);
    }
    
    /**
     * Delete a post by ID
     * @param id the post ID
     * @throws IllegalArgumentException if post not found
     */
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
    
    /**
     * Search posts by content
     * @param content the content to search for
     * @return list of posts containing the content
     */
    @Transactional(readOnly = true)
    public List<Post> searchPostsByContent(String content) {
        return postRepository.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(content);
    }
    
    /**
     * Search posts by content with pagination
     * @param content the content to search for
     * @param pageable pagination information
     * @return page of posts containing the content
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPostsByContent(String content, Pageable pageable) {
        return postRepository.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(content, pageable);
    }
    
    /**
     * Get posts created after a specific date
     * @param date the date to filter by
     * @return list of posts created after the date
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsAfterDate(LocalDateTime date) {
        return postRepository.findByCreatedAtAfterOrderByCreatedAtDesc(date);
    }
    
    /**
     * Get posts created between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of posts created between the dates
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }
    
    /**
     * Get the count of posts by a specific user
     * @param userId the user ID
     * @return count of posts created by the user
     */
    @Transactional(readOnly = true)
    public long getPostCountByUserId(Long userId) {
        return postRepository.countByUserId(userId);
    }
    
    /**
     * Get the feed for a specific user (posts from users they follow)
     * @param userId the user ID
     * @return list of posts from followed users
     */
    @Transactional(readOnly = true)
    public List<Post> getFeedByUserId(Long userId) {
        return postRepository.findFeedByUserId(userId);
    }
    
    /**
     * Get the feed for a specific user with pagination (posts from users they follow)
     * @param userId the user ID
     * @param pageable pagination information
     * @return page of posts from followed users
     */
    @Transactional(readOnly = true)
    public Page<Post> getFeedByUserId(Long userId, Pageable pageable) {
        return postRepository.findFeedByUserId(userId, pageable);
    }
}


