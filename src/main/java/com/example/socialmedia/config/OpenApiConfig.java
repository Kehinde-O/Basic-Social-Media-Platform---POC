package com.example.socialmedia.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Enhanced OpenAPI configuration for comprehensive Swagger documentation
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Social Media Platform API")
                        .version("2.0.0")
                        .description("""
                                A comprehensive REST API for a modern social media platform featuring:
                                
                                🔐 **Authentication & Security**
                                - JWT-based authentication
                                - Secure password hashing with BCrypt
                                - Role-based access control
                                
                                👥 **User Management**
                                - User registration and profile management
                                - Advanced user search and discovery
                                - Follow/unfollow functionality
                                
                                📝 **Content Management**
                                - Create, read, update, and delete posts
                                - Content search and filtering
                                - Personalized feed generation
                                
                                🔍 **Advanced Features**
                                - Pagination support for large datasets
                                - Date-based filtering
                                - Mutual connections discovery
                                - User suggestions algorithm
                                
                                **API Endpoints:**
                                - `/api/auth/*` - Authentication endpoints
                                - `/api/users/*` - User management
                                - `/api/posts/*` - Post operations
                                - `/api/follows/*` - Follow relationships
                                
                                **Authentication:** All endpoints (except auth) require Bearer token in Authorization header.
                                """)
                        .contact(new Contact()
                                .name("Social Media Platform Development Team")
                                .email("dev@socialmedia.com")
                                .url("https://socialmedia.com/developers"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.socialmedia.com")
                                .description("Production Server")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("User authentication and authorization endpoints"),
                        new Tag()
                                .name("User Management")
                                .description("User profile and account management operations"),
                        new Tag()
                                .name("Post Management")
                                .description("Content creation, retrieval, and management"),
                        new Tag()
                                .name("Follow Management")
                                .description("User follow relationships and social connections"),
                        new Tag()
                                .name("Like Management")
                                .description("Post like/unlike operations and analytics"),
                        new Tag()
                                .name("Comment Management")
                                .description("Post comment operations and management")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("""
                                                JWT Authentication
                                                
                                                To authenticate, include the JWT token in the Authorization header:
                                                ```
                                                Authorization: Bearer <your-jwt-token>
                                                ```
                                                
                                                **Token Format:**
                                                - Header: `{"alg": "HS256", "typ": "JWT"}`
                                                - Payload: `{"sub": "username", "iat": timestamp, "exp": timestamp}`
                                                - Signature: HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
                                                
                                                **Token Expiration:** 24 hours
                                                **Refresh:** Use `/api/auth/login` to get a new token
                                                """)));
    }
}
