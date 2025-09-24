package com.example.socialmedia.service;

import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.Comment;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.CommentRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for Comment operations
 */
@Service
@Transactional
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Create a new comment
     */
    public Comment createComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        Comment comment = new Comment(content, user, post);
        return commentRepository.save(comment);
    }
    
    /**
     * Get all comments for a post
     */
    public List<Comment> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }
    
    /**
     * Get all comments for a post with pagination
     */
    public Page<Comment> getCommentsForPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return commentRepository.findByPostOrderByCreatedAtAsc(post, pageable);
    }
    
    /**
     * Get all comments by a user
     */
    public List<Comment> getCommentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return commentRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    /**
     * Get all comments by a user with pagination
     */
    public Page<Comment> getCommentsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return commentRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }
    
    /**
     * Get comment by ID
     */
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }
    
    /**
     * Update a comment
     */
    public Comment updateComment(Long commentId, String newContent) {
        Comment comment = getCommentById(commentId);
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }
    
    /**
     * Delete a comment
     */
    public void deleteComment(Long commentId) {
        Comment comment = getCommentById(commentId);
        commentRepository.delete(comment);
    }
    
    /**
     * Get comment count for a post
     */
    public long getCommentCountForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        
        return commentRepository.countByPost(post);
    }
    
    /**
     * Get comment count by a user
     */
    public long getCommentCountByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return commentRepository.countByUser(user);
    }
    
    /**
     * Search comments by content
     */
    public List<Comment> searchCommentsByContent(String searchText) {
        return commentRepository.findByContentContaining(searchText);
    }
    
    /**
     * Get recent comments across all posts
     */
    public List<Comment> getRecentComments() {
        return commentRepository.findRecentComments();
    }
    
    /**
     * Get recent comments across all posts with pagination
     */
    public Page<Comment> getRecentComments(Pageable pageable) {
        return commentRepository.findRecentComments(pageable);
    }
    
    /**
     * Get comments for posts by a specific user
     */
    public List<Comment> getCommentsForUserPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        return commentRepository.findCommentsForUserPosts(user);
    }
}
