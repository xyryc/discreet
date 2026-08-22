package com.example.chatapplication.network.dto;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private UserDto user;

    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }

    public static class UserDto {
        @SerializedName("id")
        private String id;

        @SerializedName("displayName")
        private String displayName;

        @SerializedName("email")
        private String email;

        @SerializedName("handle")
        private String handle;

        @SerializedName("bio")
        private String bio;

        @SerializedName("status")
        private String status;

        @SerializedName("imageProfile")
        private String imageProfile;

        public String getId() {return id;}
        public String getDisplayName() { return displayName;}
        public String getEmail() {return email;}
        public String getHandle() {return handle;}
        public String getBio() {return bio;}
        public String getStatus() {return status;}
        public String getImageProfile() {return imageProfile;}
    }
}
