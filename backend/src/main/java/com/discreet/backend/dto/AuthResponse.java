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
        private String status;
        private String imageProfile;

        public UserDto(String id, String displayName, String email, String handle, String bio, String status,
                String imageProfile) {
            this.id = id;
            this.displayName = displayName;
            this.email = email;
            this.handle = handle;
            this.bio = bio;
            this.status = status;
            this.imageProfile = imageProfile;
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

        public String getStatus() {
            return status;
        }

        public String getImageProfile() {
            return imageProfile;
        }
    }
}
