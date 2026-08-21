package com.discreet.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.security.JwtUtils;
import com.discreet.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    // inject UserService
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    // GET /api/v1/users
    @GetMapping
    public ResponseEntity<List<AuthResponse.UserDto>> getAllUsers() {
        List<AuthResponse.UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET /api/v1/users/me (protected route)
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // check if authorization header is provided and starts with 'Bearer '
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new RuntimeException("Missing or invalid Authorization header. Expected: Bearer <token>");
        }

        // extract token string (cut off "Bearer ")
        String token = authHeader.substring(7);

        // cryptographically validate the token and expiration
        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("Invalid or expired JWT token. ");
        }

        // extract userId from token claims and fetch profile from databaseappConfig
        String userId = jwtUtils.extractUserId(token);
        AuthResponse.UserDto userProfile = userService.getUserById(userId);

        return ResponseEntity.ok(userProfile);
    }
}
