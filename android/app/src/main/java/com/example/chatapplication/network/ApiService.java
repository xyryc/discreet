package com.example.chatapplication.network;

import com.example.chatapplication.network.dto.AuthResponse;
import com.example.chatapplication.network.dto.RegisterRequest;

public interface ApiService {
    // 1. auth endpoints
    @POST("api/v1/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);
}
