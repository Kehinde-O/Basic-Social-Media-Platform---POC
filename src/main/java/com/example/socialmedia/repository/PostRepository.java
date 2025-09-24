package com.example.socialmedia.repository;

import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Post entity.
 * Provides data access methods for post operations.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Find all posts by a specific user, ordered by creation date (newest first)
     * @param user the user whose posts to retrieve
     * @return list of posts created by the user
     */
    List<Post> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Find all posts by a specific user with pagination, ordered by creation date (newest first)
     * @param user the user whose posts to retrieve
     * @param pageable pagination information
     * @return page of posts created by the user
     */
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    /**
     * Find all posts by user ID, ordered by creation date (newest first)
     * @param userId the ID of the user whose posts to retrieve
     * @return list of posts created by the user
     */
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Find all posts by user ID with pagination, ordered by creation date (newest first)
     * @param userId the ID of the user whose posts to retrieve
     * @param pageable pagination information
     * @return page of posts created by the user
     */
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * Find all posts ordered by creation date (newest first)
     * @return list of all posts
     */
    List<Post> findAllByOrderByCreatedAtDesc();
    
    /**
     * Find all posts with pagination, ordered by creation date (newest first)
     * @param pageable pagination information
     * @return page of all posts
     */
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Find posts containing specific text in content (case-insensitive)
     * @param content the text to search for
     * @return list of posts containing the text
     */
    List<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String content);
    
    /**
     * Find posts containing specific text in content with pagination (case-insensitive)
     * @param content the text to search for
     * @param pageable pagination information
     * @return page of posts containing the text
     */
    Page<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String content, Pageable pageable);
    
    /**
     * Find posts created after a specific date
     * @param date the date to filter by
     * @return list of posts created after the date
     */
    List<Post> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);
    
    /**
     * Find posts created between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of posts created between the dates
     */
    List<Post> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Count posts by a specific user
     * @param userId the ID of the user
     * @return count of posts created by the user
     */
    long countByUserId(Long userId);
    
    /**
     * Find posts from users that a specific user follows (feed)
     * @param userId the ID of the user whose feed to retrieve
     * @return list of posts from followed users
     */
    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
           "(SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) " +
           "ORDER BY p.createdAt DESC")
    List<Post> findFeedByUserId(@Param("userId") Long userId);
    
    /**
     * Find posts from users that a specific user follows with pagination (feed)
     * @param userId the ID of the user whose feed to retrieve
     * @param pageable pagination information
     * @return page of posts from followed users
     */
    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
           "(SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findFeedByUserId(@Param("userId") Long userId, Pageable pageable);
}


