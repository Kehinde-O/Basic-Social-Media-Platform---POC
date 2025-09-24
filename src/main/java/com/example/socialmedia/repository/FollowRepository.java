package com.example.socialmedia.repository;

import com.example.socialmedia.model.Follow;
import com.example.socialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Follow entity.
 * Provides data access methods for follow relationship operations.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    /**
     * Find a follow relationship between two specific users
     * @param follower the user who is following
     * @param following the user being followed
     * @return Optional containing the follow relationship if it exists
     */
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    
    /**
     * Check if a follow relationship exists between two users
     * @param followerId the ID of the follower
     * @param followingId the ID of the user being followed
     * @return true if the relationship exists, false otherwise
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * Find all users that a specific user is following
     * @param follower the user whose following list to retrieve
     * @return list of follow relationships
     */
    List<Follow> findByFollower(User follower);
    
    /**
     * Find all users that follow a specific user
     * @param following the user whose followers to retrieve
     * @return list of follow relationships
     */
    List<Follow> findByFollowing(User following);
    
    /**
     * Find all users that a specific user is following by user ID
     * @param followerId the ID of the follower
     * @return list of follow relationships
     */
    List<Follow> findByFollowerId(Long followerId);
    
    /**
     * Find all users that follow a specific user by user ID
     * @param followingId the ID of the user being followed
     * @return list of follow relationships
     */
    List<Follow> findByFollowingId(Long followingId);
    
    /**
     * Count the number of users that a specific user is following
     * @param followerId the ID of the follower
     * @return count of users being followed
     */
    long countByFollowerId(Long followerId);
    
    /**
     * Count the number of users that follow a specific user
     * @param followingId the ID of the user being followed
     * @return count of followers
     */
    long countByFollowingId(Long followingId);
    
    /**
     * Delete a follow relationship between two users
     * @param followerId the ID of the follower
     * @param followingId the ID of the user being followed
     */
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    /**
     * Find mutual follows between two users (users that both follow)
     * @param userId1 the ID of the first user
     * @param userId2 the ID of the second user
     * @return list of users that both users follow
     */
    @Query("SELECT f1.following FROM Follow f1 " +
           "WHERE f1.follower.id = :userId1 " +
           "AND f1.following.id IN " +
           "(SELECT f2.following.id FROM Follow f2 WHERE f2.follower.id = :userId2)")
    List<User> findMutualFollows(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    /**
     * Find suggested users to follow (users followed by users that the current user follows)
     * @param userId the ID of the current user
     * @return list of suggested users
     */
    @Query("SELECT DISTINCT f2.following FROM Follow f1 " +
           "JOIN Follow f2 ON f1.following.id = f2.follower.id " +
           "WHERE f1.follower.id = :userId " +
           "AND f2.following.id != :userId " +
           "AND f2.following.id NOT IN " +
           "(SELECT f3.following.id FROM Follow f3 WHERE f3.follower.id = :userId)")
    List<User> findSuggestedUsers(@Param("userId") Long userId);
}


