package com.example.chatapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.Resource;
import com.example.chatapplication.model.User;

public interface AuthRepository {
    LiveData<Resource<User>> login(String email, String password);
    LiveData<Resource<User>> register(String name, String email, String password);
    LiveData<Resource<User>> guestLogin();
}
