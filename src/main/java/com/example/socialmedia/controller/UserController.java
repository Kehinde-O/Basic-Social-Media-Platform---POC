package com.example.socialmedia.controller;

import com.example.socialmedia.dto.UserResponse;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for User operations.
 * Handles HTTP requests related to user management.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Management", description = "User management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Constructor injection for UserService
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Create a new user
     * @param user the user to create
     * @return ResponseEntity containing the created user
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Get all users
     * @return ResponseEntity containing list of all users
     */
    @Operation(summary = "Get All Users", description = "Retrieve a list of all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Get a user by ID
     * @param id the user ID
     * @return ResponseEntity containing the user if found
     */
    @Operation(summary = "Get User by ID", description = "Retrieve a specific user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(new UserResponse(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get a user by username
     * @param username the username
     * @return ResponseEntity containing the user if found
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(new UserResponse(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get a user by email
     * @param email the email
     * @return ResponseEntity containing the user if found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(new UserResponse(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Update an existing user
     * @param id the user ID
     * @param userDetails the updated user details
     * @return ResponseEntity containing the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(new UserResponse(updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Delete a user by ID
     * @param id the user ID
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Check if a username exists
     * @param username the username to check
     * @return ResponseEntity containing boolean result
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Check if an email exists
     * @param email the email to check
     * @return ResponseEntity containing boolean result
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Search users by first name
     * @param firstName the first name to search for
     * @return ResponseEntity containing list of matching users
     */
    @GetMapping("/search/firstname/{firstName}")
    public ResponseEntity<List<UserResponse>> searchUsersByFirstName(@PathVariable String firstName) {
        List<User> users = userService.searchUsersByFirstName(firstName);
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Search users by last name
     * @param lastName the last name to search for
     * @return ResponseEntity containing list of matching users
     */
    @GetMapping("/search/lastname/{lastName}")
    public ResponseEntity<List<UserResponse>> searchUsersByLastName(@PathVariable String lastName) {
        List<User> users = userService.searchUsersByLastName(lastName);
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Search users by username
     * @param username the username to search for
     * @return ResponseEntity containing list of matching users
     */
    @GetMapping("/search/username/{username}")
    public ResponseEntity<List<UserResponse>> searchUsersByUsername(@PathVariable String username) {
        List<User> users = userService.searchUsersByUsername(username);
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Get users that a specific user is following
     * @param id the user ID
     * @return ResponseEntity containing list of users being followed
     */
    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long id) {
        List<User> following = userService.getFollowing(id);
        List<UserResponse> userResponses = following.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Get users that follow a specific user
     * @param id the user ID
     * @return ResponseEntity containing list of followers
     */
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long id) {
        List<User> followers = userService.getFollowers(id);
        List<UserResponse> userResponses = followers.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }
    
    /**
     * Get the count of users that a specific user is following
     * @param id the user ID
     * @return ResponseEntity containing the following count
     */
    @GetMapping("/{id}/following/count")
    public ResponseEntity<Long> getFollowingCount(@PathVariable Long id) {
        long count = userService.getFollowingCount(id);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get the count of users that follow a specific user
     * @param id the user ID
     * @return ResponseEntity containing the followers count
     */
    @GetMapping("/{id}/followers/count")
    public ResponseEntity<Long> getFollowersCount(@PathVariable Long id) {
        long count = userService.getFollowersCount(id);
        return ResponseEntity.ok(count);
    }
}
