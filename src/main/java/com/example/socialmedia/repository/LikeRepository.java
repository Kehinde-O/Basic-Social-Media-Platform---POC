package com.example.socialmedia.repository;

import com.example.socialmedia.model.Like;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Like entity operations
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    /**
     * Find like by user and post
     */
    Optional<Like> findByUserAndPost(User user, Post post);
    
    /**
     * Check if user has liked a post
     */
    boolean existsByUserAndPost(User user, Post post);
    
    /**
     * Find all likes for a specific post
     */
    List<Like> findByPost(Post post);
    
    /**
     * Find all likes by a specific user
     */
    List<Like> findByUser(User user);
    
    /**
     * Count likes for a specific post
     */
    long countByPost(Post post);
    
    /**
     * Count likes by a specific user
     */
    long countByUser(User user);
    
    /**
     * Delete like by user and post
     */
    void deleteByUserAndPost(User user, Post post);
    
    /**
     * Find most liked posts
     */
    @Query("SELECT l.post, COUNT(l) as likeCount FROM Like l GROUP BY l.post ORDER BY likeCount DESC")
    List<Object[]> findMostLikedPosts();
    
    /**
     * Find likes for posts by a specific user
     */
    @Query("SELECT l FROM Like l WHERE l.post.user = :user")
    List<Like> findLikesForUserPosts(@Param("user") User user);
}
