package com.example.chatapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.User;

import java.util.List;

public interface UserRepository {
    LiveData<List<User>> getContacts();
    LiveData<User> getCurrentUser();
    void updateUserProfile(String name, String bio, String image);
    void refreshContacts();
}
