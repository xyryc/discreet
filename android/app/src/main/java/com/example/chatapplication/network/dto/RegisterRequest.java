package com.example.chatapplication.network.dto;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("displayName")
    private String displayName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public RegisterRequest(String displayName, String email, String password){
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName() {return displayName;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
}
