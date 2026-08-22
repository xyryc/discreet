package com.discreet.backend.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.dto.UpdateProfileRequest;
import com.discreet.backend.security.JwtUtils;
import com.discreet.backend.service.StorageService;
import com.discreet.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final StorageService storageService;

    // inject UserService
    public UserController(UserService userService, JwtUtils jwtUtils, StorageService storageService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.storageService = storageService;
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

    // PUT /api/v1/users/me (protected route)
    @PutMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> updateProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody UpdateProfileRequest request) {
        // check authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header. Expected: Bearer <token>");
        }

        // extract token string (cut off "Bearer ")
        String token = authHeader.substring(7);

        // cryptographically validate the token
        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("Invalid or expired JWT token.");
        }

        // extract userId and update profile in PostgreSQL/H2
        String userId = jwtUtils.extractUserId(token);
        AuthResponse.UserDto updatedProfile = userService.updateProfile(userId, request);

        return ResponseEntity.ok(updatedProfile);
    }

    // POST /api/v1/users/me/avatar (multi-part file upload)
    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse.UserDto> uploadAvatar(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam("file") MultipartFile file) {
        // 1. check authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header. Expected: Bearer <token>");
        }

        // 2. extract and validate token
        String token = authHeader.substring(7);
        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("Invalid or expired JWT token.");
        }

        // 3. extract userId, upload file to s3 and update profile
        String userId = jwtUtils.extractUserId(token);
        String avatarUrl = storageService.uploadAvatar(file, userId);
        AuthResponse.UserDto updatedProfile = userService.updateAvatar(userId, avatarUrl);

        return ResponseEntity.ok(updatedProfile);
    }
}