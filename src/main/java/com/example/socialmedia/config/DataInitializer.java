package com.example.socialmedia.config;

import com.example.socialmedia.model.*;
import com.example.socialmedia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Data initializer component that populates the database with sample data
 * when the application starts. This helps demonstrate the functionality
 * of the social media platform.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Constructor injection for repositories and password encoder
     * @param userRepository the user repository
     * @param postRepository the post repository
     * @param followRepository the follow repository
     * @param likeRepository the like repository
     * @param commentRepository the comment repository
     * @param passwordEncoder the password encoder
     */
    @Autowired
    public DataInitializer(UserRepository userRepository, 
                          PostRepository postRepository, 
                          FollowRepository followRepository,
                          LikeRepository likeRepository,
                          CommentRepository commentRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Initialize sample data when the application starts
     * @param args command line arguments
     */
    @Override
    public void run(String... args) {
        // Only initialize data if the database is empty
        if (userRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    /**
     * Create sample users, posts, and follow relationships
     */
    private void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        // Create sample users with hashed passwords
        User alice = new User("alice_smith", "alice.smith@email.com", "password123", "Alice", "Smith");
        alice.setBio("Software developer passionate about technology and innovation.");
        alice.setPassword(passwordEncoder.encode(alice.getPassword()));
        userRepository.save(alice);
        
        User bob = new User("bob_jones", "bob.jones@email.com", "password123", "Bob", "Jones");
        bob.setBio("Digital marketing specialist and social media enthusiast.");
        bob.setPassword(passwordEncoder.encode(bob.getPassword()));
        userRepository.save(bob);
        
        User charlie = new User("charlie_brown", "charlie.brown@email.com", "password123", "Charlie", "Brown");
        charlie.setBio("Photographer capturing moments and telling stories through images.");
        charlie.setPassword(passwordEncoder.encode(charlie.getPassword()));
        userRepository.save(charlie);
        
        User diana = new User("diana_prince", "diana.prince@email.com", "password123", "Diana", "Prince");
        diana.setBio("Fitness trainer and wellness coach helping people achieve their goals.");
        diana.setPassword(passwordEncoder.encode(diana.getPassword()));
        userRepository.save(diana);
        
        User eve = new User("eve_wilson", "eve.wilson@email.com", "password123", "Eve", "Wilson");
        eve.setBio("Travel blogger exploring the world one destination at a time.");
        eve.setPassword(passwordEncoder.encode(eve.getPassword()));
        userRepository.save(eve);
        
        // Create sample posts
        Post post1 = new Post("Just finished working on an exciting new project! Can't wait to share more details soon. #coding #innovation", alice);
        postRepository.save(post1);
        
        Post post2 = new Post("Beautiful sunset from my evening walk. Sometimes the best moments are the simplest ones. 🌅", charlie);
        postRepository.save(post2);
        
        Post post3 = new Post("New marketing campaign is live! Excited to see how it performs. #marketing #digital", bob);
        postRepository.save(post3);
        
        Post post4 = new Post("Morning workout complete! Remember, consistency is key to achieving your fitness goals. 💪", diana);
        postRepository.save(post4);
        
        Post post5 = new Post("Just arrived in Tokyo! The city is absolutely amazing. Can't wait to explore more. #travel #tokyo", eve);
        postRepository.save(post5);
        
        Post post6 = new Post("Coffee and code - the perfect combination for a productive day! ☕️", alice);
        postRepository.save(post6);
        
        Post post7 = new Post("Photography tip: Golden hour lighting can transform any ordinary scene into something magical. 📸", charlie);
        postRepository.save(post7);
        
        Post post8 = new Post("Analyzing social media metrics from this week. Data-driven decisions lead to better results! 📊", bob);
        postRepository.save(post8);
        
        Post post9 = new Post("Healthy meal prep Sunday! Planning and preparation make healthy eating so much easier. 🥗", diana);
        postRepository.save(post9);
        
        Post post10 = new Post("Met some amazing locals today who shared incredible stories about their culture. Travel is about connections! 🌍", eve);
        postRepository.save(post10);
        
        // Create follow relationships
        Follow follow1 = new Follow(alice, bob);
        followRepository.save(follow1);
        
        Follow follow2 = new Follow(alice, charlie);
        followRepository.save(follow2);
        
        Follow follow3 = new Follow(bob, alice);
        followRepository.save(follow3);
        
        Follow follow4 = new Follow(bob, diana);
        followRepository.save(follow4);
        
        Follow follow5 = new Follow(charlie, alice);
        followRepository.save(follow5);
        
        Follow follow6 = new Follow(charlie, eve);
        followRepository.save(follow6);
        
        Follow follow7 = new Follow(diana, bob);
        followRepository.save(follow7);
        
        Follow follow8 = new Follow(diana, charlie);
        followRepository.save(follow8);
        
        Follow follow9 = new Follow(eve, charlie);
        followRepository.save(follow9);
        
        Follow follow10 = new Follow(eve, diana);
        followRepository.save(follow10);
        
        // Create sample likes
        Like like1 = new Like(bob, post1);
        likeRepository.save(like1);
        
        Like like2 = new Like(charlie, post1);
        likeRepository.save(like2);
        
        Like like3 = new Like(alice, post2);
        likeRepository.save(like3);
        
        Like like4 = new Like(diana, post2);
        likeRepository.save(like4);
        
        Like like5 = new Like(eve, post2);
        likeRepository.save(like5);
        
        Like like6 = new Like(alice, post3);
        likeRepository.save(like6);
        
        Like like7 = new Like(charlie, post4);
        likeRepository.save(like7);
        
        Like like8 = new Like(bob, post5);
        likeRepository.save(like8);
        
        Like like9 = new Like(diana, post5);
        likeRepository.save(like9);
        
        Like like10 = new Like(eve, post6);
        likeRepository.save(like10);
        
        // Create sample comments
        Comment comment1 = new Comment("Great work on the project! Looking forward to seeing more details.", bob, post1);
        commentRepository.save(comment1);
        
        Comment comment2 = new Comment("Amazing sunset! Your photography skills are incredible.", alice, post2);
        commentRepository.save(comment2);
        
        Comment comment3 = new Comment("Thanks for sharing! This is really helpful.", charlie, post3);
        commentRepository.save(comment3);
        
        Comment comment4 = new Comment("You're absolutely right! Consistency is everything in fitness.", eve, post4);
        commentRepository.save(comment4);
        
        Comment comment5 = new Comment("Tokyo is amazing! Have you tried the ramen there?", diana, post5);
        commentRepository.save(comment5);
        
        Comment comment6 = new Comment("Coffee and code - my favorite combination too! ☕️", bob, post6);
        commentRepository.save(comment6);
        
        Comment comment7 = new Comment("Great tip! Golden hour really does make everything look magical.", alice, post7);
        commentRepository.save(comment7);
        
        Comment comment8 = new Comment("Data-driven decisions are the way to go! 📊", charlie, post8);
        commentRepository.save(comment8);
        
        Comment comment9 = new Comment("Meal prep is a game changer! Thanks for the inspiration.", eve, post9);
        commentRepository.save(comment9);
        
        Comment comment10 = new Comment("Travel connections are the best part of exploring new places!", bob, post10);
        commentRepository.save(comment10);
        
        System.out.println("Sample data initialization completed!");
        System.out.println("Created " + userRepository.count() + " users");
        System.out.println("Created " + postRepository.count() + " posts");
        System.out.println("Created " + followRepository.count() + " follow relationships");
        System.out.println("Created " + likeRepository.count() + " likes");
        System.out.println("Created " + commentRepository.count() + " comments");
    }
}
