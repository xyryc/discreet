package com.discreet.backend.dto;

public class AuthResponse {
    private String token;
    private UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }

    public static class UserDto {
        private String id;
        private String displayName;
        private String email;
        private String handle;
        private String bio;

        public UserDto(String id, String displayName, String email, String handle, String bio) {
            this.id = id;
            this.displayName = displayName;
            this.email = email;
            this.handle = handle;
            this.bio = bio;
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmail() {
            return email;
        }

        public String getHandle() {
            return handle;
        }

        public String getBio() {
            return bio;
        }
    }
}
