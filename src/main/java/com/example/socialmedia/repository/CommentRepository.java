package com.example.socialmedia.repository;

import com.example.socialmedia.model.Comment;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity operations
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * Find all comments for a specific post
     */
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    
    /**
     * Find all comments for a specific post with pagination
     */
    Page<Comment> findByPostOrderByCreatedAtAsc(Post post, Pageable pageable);
    
    /**
     * Find all comments by a specific user
     */
    List<Comment> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Find all comments by a specific user with pagination
     */
    Page<Comment> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    
    /**
     * Count comments for a specific post
     */
    long countByPost(Post post);
    
    /**
     * Count comments by a specific user
     */
    long countByUser(User user);
    
    /**
     * Find comments containing specific text
     */
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:text% ORDER BY c.createdAt DESC")
    List<Comment> findByContentContaining(@Param("text") String text);
    
    /**
     * Find comments for posts by a specific user
     */
    @Query("SELECT c FROM Comment c WHERE c.post.user = :user ORDER BY c.createdAt DESC")
    List<Comment> findCommentsForUserPosts(@Param("user") User user);
    
    /**
     * Find recent comments across all posts
     */
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    List<Comment> findRecentComments();
    
    /**
     * Find recent comments across all posts with pagination
     */
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    Page<Comment> findRecentComments(Pageable pageable);
}
