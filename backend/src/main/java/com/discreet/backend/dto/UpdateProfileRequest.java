package com.discreet.backend.dto;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {
    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;

    @Size(max = 250, message = "Bio can not exceed 200 characters")
    private String bio;

    private String status; // ONLINE, BUSY, DISCREET_SHIELD, OFFLINE
    private String imageProfile; // Avatar URL or Base64 avatar placeholder

    public UpdateProfileRequest() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
