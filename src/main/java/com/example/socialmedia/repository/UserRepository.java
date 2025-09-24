package com.example.socialmedia.repository;

import com.example.socialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides data access methods for user operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by username
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a username exists
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if an email exists
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by first name containing the given string (case-insensitive)
     * @param firstName the first name to search for
     * @return list of users matching the criteria
     */
    List<User> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find users by last name containing the given string (case-insensitive)
     * @param lastName the last name to search for
     * @return list of users matching the criteria
     */
    List<User> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Find users by username containing the given string (case-insensitive)
     * @param username the username to search for
     * @return list of users matching the criteria
     */
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    /**
     * Find users that a specific user is following
     * @param userId the ID of the user whose following list to retrieve
     * @return list of users that the specified user is following
     */
    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId")
    List<User> findFollowingByUserId(@Param("userId") Long userId);
    
    /**
     * Find users that follow a specific user
     * @param userId the ID of the user whose followers to retrieve
     * @return list of users that follow the specified user
     */
    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);
    
    /**
     * Count the number of users that a specific user is following
     * @param userId the ID of the user
     * @return count of users being followed
     */
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    long countFollowingByUserId(@Param("userId") Long userId);
    
    /**
     * Count the number of users that follow a specific user
     * @param userId the ID of the user
     * @return count of followers
     */
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    long countFollowersByUserId(@Param("userId") Long userId);
}


