package com.example.chatapplication.network.dto;

public class UpdateProfileRequest {
    @SerializedName("displayName")
    private String displayName;

    @SerializedName("bio")
    private String bio;

    @SerializedName("status")
    private String status;

    @SerializedName("imageProfile")
    private String imageProfile;

    public UpdateProfileRequest(String displayName, String bio, String status, String imageProfile){
        this.displayName = displayName;
        this.bio = bio;
        this.status = status;
        this.imageProfile = imageProfile;
    }

    public String getDisplayName(){return displayName;}
    public String getBio(){return bio;}
    public String getStatus(){return status;}
    public String getImageProfile(){return imageProfile;}
}
