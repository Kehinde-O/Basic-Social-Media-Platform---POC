package com.example.socialmedia.service;

import com.example.socialmedia.model.Follow;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.FollowRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Follow entity operations.
 * Contains business logic for follow relationship management.
 */
@Service
@Transactional
public class FollowService {
    
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    
    /**
     * Constructor injection for repositories
     * @param followRepository the follow repository
     * @param userRepository the user repository
     */
    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Follow a user
     * @param followerId the ID of the user who wants to follow
     * @param followingId the ID of the user to be followed
     * @return the created follow relationship
     * @throws IllegalArgumentException if users not found or invalid operation
     */
    public Follow followUser(Long followerId, Long followingId) {
        // Validate that users exist
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found with id: " + followerId));
        
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("User to follow not found with id: " + followingId));
        
        // Check if trying to follow self
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        
        // Check if already following
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("Already following this user");
        }
        
        Follow follow = new Follow(follower, following);
        return followRepository.save(follow);
    }
    
    /**
     * Unfollow a user
     * @param followerId the ID of the user who wants to unfollow
     * @param followingId the ID of the user to be unfollowed
     * @throws IllegalArgumentException if users not found or not following
     */
    public void unfollowUser(Long followerId, Long followingId) {
        // Validate that users exist
        if (!userRepository.existsById(followerId)) {
            throw new IllegalArgumentException("Follower not found with id: " + followerId);
        }
        
        if (!userRepository.existsById(followingId)) {
            throw new IllegalArgumentException("User to unfollow not found with id: " + followingId);
        }
        
        // Check if following relationship exists
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalArgumentException("Not following this user");
        }
        
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    /**
     * Check if a user is following another user
     * @param followerId the ID of the potential follower
     * @param followingId the ID of the potential user being followed
     * @return true if following, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
    
    /**
     * Get all users that a specific user is following
     * @param userId the user ID
     * @return list of users being followed
     */
    @Transactional(readOnly = true)
    public List<User> getFollowing(Long userId) {
        return userRepository.findFollowingByUserId(userId);
    }
    
    /**
     * Get all users that follow a specific user
     * @param userId the user ID
     * @return list of followers
     */
    @Transactional(readOnly = true)
    public List<User> getFollowers(Long userId) {
        return userRepository.findFollowersByUserId(userId);
    }
    
    /**
     * Get all follow relationships where a user is the follower
     * @param userId the user ID
     * @return list of follow relationships
     */
    @Transactional(readOnly = true)
    public List<Follow> getFollowingRelationships(Long userId) {
        return followRepository.findByFollowerId(userId);
    }
    
    /**
     * Get all follow relationships where a user is being followed
     * @param userId the user ID
     * @return list of follow relationships
     */
    @Transactional(readOnly = true)
    public List<Follow> getFollowerRelationships(Long userId) {
        return followRepository.findByFollowingId(userId);
    }
    
    /**
     * Get the count of users that a specific user is following
     * @param userId the user ID
     * @return count of users being followed
     */
    @Transactional(readOnly = true)
    public long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
    
    /**
     * Get the count of users that follow a specific user
     * @param userId the user ID
     * @return count of followers
     */
    @Transactional(readOnly = true)
    public long getFollowersCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
    
    /**
     * Get mutual follows between two users (users that both follow)
     * @param userId1 the ID of the first user
     * @param userId2 the ID of the second user
     * @return list of users that both users follow
     */
    @Transactional(readOnly = true)
    public List<User> getMutualFollows(Long userId1, Long userId2) {
        return followRepository.findMutualFollows(userId1, userId2);
    }
    
    /**
     * Get suggested users to follow (users followed by users that the current user follows)
     * @param userId the ID of the current user
     * @return list of suggested users
     */
    @Transactional(readOnly = true)
    public List<User> getSuggestedUsers(Long userId) {
        return followRepository.findSuggestedUsers(userId);
    }
    
    /**
     * Get a specific follow relationship
     * @param followerId the ID of the follower
     * @param followingId the ID of the user being followed
     * @return Optional containing the follow relationship if it exists
     */
    @Transactional(readOnly = true)
    public Optional<Follow> getFollowRelationship(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).orElse(null);
        User following = userRepository.findById(followingId).orElse(null);
        
        if (follower == null || following == null) {
            return Optional.empty();
        }
        
        return followRepository.findByFollowerAndFollowing(follower, following);
    }
}


