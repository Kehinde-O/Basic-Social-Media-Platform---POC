# Basic Social Media Platform

## Project Description

This project is a comprehensive social media platform built using Spring Boot and H2 database. The platform features JWT-based authentication, complete user management, post system, follow relationships, and comprehensive API documentation. It demonstrates modern web development practices with security, documentation, and clean architecture.

### Key Features

- **JWT Authentication**: Secure user authentication and authorization using JSON Web Tokens
- **User Management**: Create, read, update, and delete user profiles with password hashing
- **Post System**: Users can create and manage posts with content up to 1000 characters
- **Follow System**: Users can follow and unfollow other users to build their network
- **Like System**: Users can like and unlike posts with analytics and engagement tracking
- **Comment System**: Users can comment on posts with full CRUD operations
- **Feed Generation**: Users can view posts from users they follow in their personalized feed
- **Search Functionality**: Search for users by name or username, and search posts by content
- **RESTful API**: Complete REST API with proper HTTP status codes and error handling
- **API Documentation**: Interactive Swagger/OpenAPI documentation with authentication support
- **Data Validation**: Input validation using Bean Validation annotations
- **Pagination Support**: Paginated endpoints for better performance with large datasets
- **Security**: Spring Security integration with JWT token validation
- **Error Handling**: Global exception handling with structured error responses
- **Analytics**: Like counts, comment counts, and follow statistics

### Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **Database**: H2 In-Memory Database
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **Build Tool**: Maven
- **Java Version**: 17
- **Validation**: Bean Validation (Jakarta Validation)
- **Password Hashing**: BCrypt

## Project Structure

```
Basic Social Media Platform/
├── src/
│   ├── main/
│   │   ├── java/com/example/socialmedia/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java          # Sample data initialization
│   │   │   │   └── OpenApiConfig.java            # Swagger/OpenAPI configuration
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java           # Authentication endpoints
│   │   │   │   ├── UserController.java           # User REST endpoints
│   │   │   │   ├── PostController.java           # Post REST endpoints
│   │   │   │   └── FollowController.java         # Follow relationship endpoints
│   │   │   ├── dto/
│   │   │   │   ├── AuthResponse.java             # Authentication response DTO
│   │   │   │   ├── LoginRequest.java             # Login request DTO
│   │   │   │   └── RegisterRequest.java          # Registration request DTO
│   │   │   ├── model/
│   │   │   │   ├── User.java                     # User entity
│   │   │   │   ├── Post.java                     # Post entity
│   │   │   │   └── Follow.java                   # Follow relationship entity
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java           # User data access
│   │   │   │   ├── PostRepository.java           # Post data access
│   │   │   │   └── FollowRepository.java         # Follow relationship data access
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthenticationFilter.java  # JWT authentication filter
│   │   │   │   └── SecurityConfig.java           # Spring Security configuration
│   │   │   ├── service/
│   │   │   │   ├── UserService.java              # User business logic
│   │   │   │   ├── PostService.java              # Post business logic
│   │   │   │   └── FollowService.java            # Follow relationship business logic
│   │   │   ├── util/
│   │   │   │   └── JwtUtils.java                 # JWT utility class
│   │   │   └── SocialMediaApplication.java       # Main Spring Boot application
│   │   └── resources/
│   │       └── application.properties            # Application configuration
│   └── test/
│       └── java/com/example/socialmedia/         # Test classes (to be implemented)
├── pom.xml                                       # Maven configuration
└── README.md                                     # This file
```

## Database Schema

### Users Table
- `id` (Primary Key, Auto-generated)
- `username` (Unique, Not Null)
- `email` (Unique, Not Null)
- `password` (Not Null, BCrypt hashed)
- `first_name` (Not Null)
- `last_name` (Not Null)
- `bio` (Optional, Max 500 characters)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

### Posts Table
- `id` (Primary Key, Auto-generated)
- `content` (Not Null, Max 1000 characters)
- `user_id` (Foreign Key to Users)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)

### Follows Table
- `id` (Primary Key, Auto-generated)
- `follower_id` (Foreign Key to Users)
- `following_id` (Foreign Key to Users)
- `created_at` (Timestamp)
- Unique constraint on (follower_id, following_id)

## API Endpoints

### Authentication Endpoints
- `POST /api/auth/register` - Register a new user account
- `POST /api/auth/login` - Login with username/email and password
- `POST /api/auth/validate` - Validate JWT token

### User Endpoints (Protected - Requires JWT Token)
- `POST /api/users` - Create a new user
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/email/{email}` - Get user by email
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/search/firstname/{firstName}` - Search users by first name
- `GET /api/users/search/lastname/{lastName}` - Search users by last name
- `GET /api/users/search/username/{username}` - Search users by username
- `GET /api/users/{id}/following` - Get users that this user follows
- `GET /api/users/{id}/followers` - Get users that follow this user
- `GET /api/users/{id}/following/count` - Get following count
- `GET /api/users/{id}/followers/count` - Get followers count

### Post Endpoints (Protected - Requires JWT Token)
- `POST /api/posts` - Create a new post
- `POST /api/posts/user/{userId}` - Create a post for a specific user
- `GET /api/posts` - Get all posts
- `GET /api/posts/paginated` - Get all posts with pagination
- `GET /api/posts/{id}` - Get post by ID
- `GET /api/posts/user/{userId}` - Get posts by user
- `GET /api/posts/user/{userId}/paginated` - Get posts by user with pagination
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post
- `GET /api/posts/search` - Search posts by content
- `GET /api/posts/search/paginated` - Search posts by content with pagination
- `GET /api/posts/after/{date}` - Get posts created after a specific date
- `GET /api/posts/between/{startDate}/{endDate}` - Get posts between two dates
- `GET /api/posts/feed/{userId}` - Get user's feed (posts from followed users)
- `GET /api/posts/feed/{userId}/paginated` - Get user's feed with pagination
- `GET /api/posts/user/{userId}/count` - Get post count by user

### Follow Endpoints (Protected - Requires JWT Token)
- `POST /api/follows/{followerId}/follow/{followingId}` - Follow a user
- `DELETE /api/follows/{followerId}/unfollow/{followingId}` - Unfollow a user
- `GET /api/follows/{followerId}/following/{followingId}` - Check if following
- `GET /api/follows/{userId}/following` - Get users that this user follows
- `GET /api/follows/{userId}/followers` - Get users that follow this user
- `GET /api/follows/{userId}/following/relationships` - Get follow relationships where user is follower
- `GET /api/follows/{userId}/followers/relationships` - Get follow relationships where user is being followed
- `GET /api/follows/{userId}/following/count` - Get following count
- `GET /api/follows/{userId}/followers/count` - Get followers count
- `GET /api/follows/{userId1}/mutual/{userId2}` - Get mutual follows
- `GET /api/follows/{userId}/suggestions` - Get suggested users to follow
- `GET /api/follows/{followerId}/relationship/{followingId}` - Get specific follow relationship

### Like Endpoints (Protected - Requires JWT Token)
- `POST /api/likes/{userId}/like/{postId}` - Like a post
- `DELETE /api/likes/{userId}/unlike/{postId}` - Unlike a post
- `POST /api/likes/{userId}/toggle/{postId}` - Toggle like status
- `GET /api/likes/{userId}/has-liked/{postId}` - Check if user liked a post
- `GET /api/likes/post/{postId}` - Get all likes for a post
- `GET /api/likes/user/{userId}` - Get all likes by a user
- `GET /api/likes/post/{postId}/count` - Get like count for a post
- `GET /api/likes/user/{userId}/count` - Get like count by a user
- `GET /api/likes/most-liked` - Get most liked posts

### Comment Endpoints (Protected - Requires JWT Token)
- `POST /api/comments/{userId}/comment/{postId}` - Create a comment
- `GET /api/comments/post/{postId}` - Get all comments for a post
- `GET /api/comments/post/{postId}/paginated` - Get comments for a post with pagination
- `GET /api/comments/user/{userId}` - Get all comments by a user
- `GET /api/comments/{commentId}` - Get comment by ID
- `PUT /api/comments/{commentId}` - Update a comment
- `DELETE /api/comments/{commentId}` - Delete a comment
- `GET /api/comments/post/{postId}/count` - Get comment count for a post
- `GET /api/comments/search` - Search comments by content
- `GET /api/comments/recent` - Get recent comments across all posts

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- Git (for cloning the repository)

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd "Basic Social Media Platform"
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   
   **Option 1: Using the startup script (recommended)**
   ```bash
   ./run.sh
   ```
   
   **Option 2: Using Maven directly**
   ```bash
   mvn spring-boot:run
   ```
   
   **Option 3: With JVM arguments to suppress warnings**
   ```bash
   export MAVEN_OPTS="--enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.desktop/java.awt.font=ALL-UNNAMED"
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
   - H2 Database Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:socialmedia`
     - Username: `sa`
     - Password: (leave empty)

### Sample Data

The application automatically initializes with sample data including:
- 5 sample users with different profiles (password: `password123`)
- 10 sample posts from various users
- Follow relationships between users

### Authentication

The application uses JWT (JSON Web Tokens) for authentication. To access protected endpoints:

1. **Register a new user** or **login** with existing credentials
2. **Copy the JWT token** from the response
3. **Include the token** in the Authorization header: `Bearer <your-jwt-token>`

**Sample Users for Testing:**
- Username: `alice_smith`, Password: `password123`
- Username: `bob_jones`, Password: `password123`
- Username: `charlie_brown`, Password: `password123`
- Username: `diana_prince`, Password: `password123`
- Username: `eve_wilson`, Password: `password123`

## Usage Examples

### User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john.doe@email.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "bio": "Software developer and tech enthusiast"
  }'
```

### User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "alice_smith",
    "password": "password123"
  }'
```

### Creating a Post (with JWT token)
```bash
curl -X POST http://localhost:8080/api/posts/user/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d "Just finished an amazing project! #coding #success"
```

### Following a User (with JWT token)
```bash
curl -X POST http://localhost:8080/api/follows/1/follow/2 \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Getting User Feed (with JWT token)
```bash
curl http://localhost:8080/api/posts/feed/1 \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Testing

### Running Tests
```bash
mvn test
```

### Manual Testing with cURL

1. **Test User Registration**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"username": "test_user", "email": "test@email.com", "password": "password123", "firstName": "Test", "lastName": "User"}'
   ```

2. **Test User Login**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"usernameOrEmail": "test_user", "password": "password123"}'
   ```

3. **Test Post Creation (with JWT token)**
   ```bash
   curl -X POST http://localhost:8080/api/posts/user/1 \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <your-jwt-token>" \
     -d "This is a test post!"
   ```

4. **Test Follow Relationship (with JWT token)**
   ```bash
   curl -X POST http://localhost:8080/api/follows/1/follow/2 \
     -H "Authorization: Bearer <your-jwt-token>"
   ```

### Swagger UI Testing

1. **Access Swagger UI**: Navigate to `http://localhost:8080/swagger-ui/index.html`
2. **Authorize**: Click the "Authorize" button and enter your JWT token
3. **Test Endpoints**: Use the interactive interface to test all API endpoints

## Code Documentation

### Key Classes and Their Responsibilities

#### Entity Classes
- **User**: Represents a user in the system with profile information and relationships
- **Post**: Represents a user's post with content and metadata
- **Follow**: Represents a follow relationship between two users

#### Repository Classes
- **UserRepository**: Data access layer for user operations with custom query methods
- **PostRepository**: Data access layer for post operations with pagination support
- **FollowRepository**: Data access layer for follow relationship operations

#### Service Classes
- **UserService**: Business logic for user management including validation
- **PostService**: Business logic for post management and feed generation
- **FollowService**: Business logic for follow relationship management

#### Controller Classes
- **AuthController**: Authentication endpoints (login, register, token validation)
- **UserController**: REST endpoints for user operations
- **PostController**: REST endpoints for post operations
- **FollowController**: REST endpoints for follow relationship operations

#### Security Classes
- **SecurityConfig**: Spring Security configuration with JWT integration
- **JwtAuthenticationFilter**: JWT token validation filter
- **JwtUtils**: JWT token generation and validation utilities

#### DTO Classes
- **LoginRequest**: Login request data transfer object
- **RegisterRequest**: Registration request data transfer object
- **AuthResponse**: Authentication response with JWT token

### Design Patterns Used

1. **Repository Pattern**: Separates data access logic from business logic
2. **Service Layer Pattern**: Encapsulates business logic and provides transaction management
3. **DTO Pattern**: Uses dedicated DTOs for API communication and authentication
4. **Dependency Injection**: Uses Spring's IoC container for dependency management
5. **Filter Pattern**: JWT authentication filter for request processing
6. **Strategy Pattern**: Password encoding strategy with BCrypt

### Validation and Error Handling

- Input validation using Bean Validation annotations
- Custom exception handling with meaningful error messages
- Proper HTTP status codes for different scenarios
- Transaction management for data consistency
- JWT token validation and security
- Password hashing with BCrypt for secure storage

## Troubleshooting

### Java Module System Warnings

If you encounter warnings like:
```
WARNING: A restricted method in java.lang.System has been called
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning
```

**Solutions:**
1. **Use the provided startup script**: `./run.sh` (recommended)
2. **Set JVM arguments manually**:
   ```bash
   export MAVEN_OPTS="--enable-native-access=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.text=ALL-UNNAMED --add-opens java.desktop/java.awt.font=ALL-UNNAMED"
   mvn spring-boot:run
   ```
3. **The warnings are harmless** - the application will work correctly even with these warnings

### Port Already in Use

If you get "Port 8080 was already in use":
```bash
# Find and kill the process using port 8080
lsof -ti:8080 | xargs kill -9

# Or use a different port
export SERVER_PORT=8081
mvn spring-boot:run
```

### Database Issues

If you encounter database connection issues:
1. **Check H2 console**: `http://localhost:8080/h2-console`
2. **Verify JDBC URL**: `jdbc:h2:mem:socialmedia`
3. **Restart the application** to reset the in-memory database

## Future Enhancements

Potential improvements for the platform:

1. **File Upload**: Support for profile pictures and post images
2. **Real-time Features**: WebSocket support for real-time notifications
3. **Advanced Search**: Full-text search with Elasticsearch
4. **Caching**: Redis integration for improved performance
5. **Unit Tests**: Comprehensive test coverage
6. **Docker Support**: Containerization for easy deployment
7. **Rate Limiting**: API rate limiting for security
8. **Email Verification**: Email verification for user registration
9. **Password Reset**: Password reset functionality
10. **Social Login**: OAuth integration with Google/Facebook

## Video Demonstration

[Link to 4-5 minute video demonstration will be added here]

*Note: The video will include a walkthrough of the application features, code explanation, and a "talking head" presentation as required for accreditation.*

## Author

**Kehinde Omotoso**
- Course: CSE 310 - Web Fundamentals
- Institution: BYU Idaho
- Project: Basic Social Media Platform

## License

This project is created for educational purposes as part of CSE 310 coursework at BYU Idaho.

---

*This project demonstrates proficiency in Java, Spring Boot, REST API development, database design, and software engineering best practices.*
