package com.discreet.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.discreet.backend.dto.AuthResponse;
import com.discreet.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    // inject UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/v1/users
    @GetMapping
    public ResponseEntity<List<AuthResponse.UserDto>> getAllUsers() {
        List<AuthResponse.UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
