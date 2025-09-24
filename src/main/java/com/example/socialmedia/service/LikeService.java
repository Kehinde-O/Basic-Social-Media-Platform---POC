package com.example.socialmedia.service;

import com.example.socialmedia.exception.BusinessLogicException;
import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.Like;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.LikeRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Like operations
 */
@Service
@Transactional
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Like a post
     */
    public Like likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        // Check if user has already liked this post
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new BusinessLogicException("You have already liked this post");
        }
        
        // Check if user is trying to like their own post
        if (post.getUser().getId().equals(userId)) {
            throw new BusinessLogicException("You cannot like your own post");
        }
        
        Like like = new Like(user, post);
        return likeRepository.save(like);
    }
    
    /**
     * Unlike a post
     */
    public void unlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new BusinessLogicException("You have not liked this post"));
        
        likeRepository.delete(like);
    }
    
    /**
     * Check if user has liked a post
     */
    public boolean hasUserLikedPost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return likeRepository.existsByUserAndPost(user, post);
    }
    
    /**
     * Get all likes for a post
     */
    public List<Like> getLikesForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return likeRepository.findByPost(post);
    }
    
    /**
     * Get all likes by a user
     */
    public List<Like> getLikesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return likeRepository.findByUser(user);
    }
    
    /**
     * Get like count for a post
     */
    public long getLikeCountForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return likeRepository.countByPost(post);
    }
    
    /**
     * Get like count by a user
     */
    public long getLikeCountByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return likeRepository.countByUser(user);
    }
    
    /**
     * Toggle like (like if not liked, unlike if liked)
     */
    public boolean toggleLike(Long userId, Long postId) {
        if (hasUserLikedPost(userId, postId)) {
            unlikePost(userId, postId);
            return false; // Post is now unliked
        } else {
            likePost(userId, postId);
            return true; // Post is now liked
        }
    }
    
    /**
     * Get most liked posts
     */
    public List<Object[]> getMostLikedPosts() {
        return likeRepository.findMostLikedPosts();
    }
}
