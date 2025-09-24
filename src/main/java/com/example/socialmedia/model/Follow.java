package com.example.socialmedia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Follow entity representing a follow relationship between two users.
 * This creates a many-to-many relationship between users with additional metadata.
 */
@Entity
@Table(name = "follows", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@JsonIgnoreProperties({"follower", "following"})
public class Follow {
    
    /**
     * Unique identifier for the follow relationship
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * The user who is following (follower)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;
    
    /**
     * The user being followed (following)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
    
    /**
     * Timestamp when the follow relationship was created
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /**
     * Default constructor
     */
    public Follow() {
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with follower and following users
     * @param follower the user who is following
     * @param following the user being followed
     */
    public Follow(User follower, User following) {
        this();
        this.follower = follower;
        this.following = following;
    }
    
    /**
     * Updates the timestamp before persisting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getFollower() {
        return follower;
    }
    
    public void setFollower(User follower) {
        this.follower = follower;
    }
    
    public User getFollowing() {
        return following;
    }
    
    public void setFollowing(User following) {
        this.following = following;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower=" + (follower != null ? follower.getUsername() : "null") +
                ", following=" + (following != null ? following.getUsername() : "null") +
                ", createdAt=" + createdAt +
                '}';
    }
}


