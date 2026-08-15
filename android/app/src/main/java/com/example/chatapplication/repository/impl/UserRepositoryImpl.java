package com.example.chatapplication.repository.impl;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.UserRepository;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static UserRepositoryImpl instance;
    private final MockDataService mockDataService;
    private final SessionManager sessionManager;
    private final MutableLiveData<List<User>> contactsLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();

    private UserRepositoryImpl(Context context) {
        this.mockDataService = MockDataService.getInstance();
        this.sessionManager = SessionManager.getInstance(context);
        refreshContacts();
        refreshCurrentUser();
    }

    public static synchronized UserRepositoryImpl getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepositoryImpl(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public LiveData<List<User>> getContacts() {
        refreshContacts();
        return contactsLiveData;
    }

    @Override
    public LiveData<User> getCurrentUser() {
        refreshCurrentUser();
        return currentUserLiveData;
    }

    @Override
    public void updateUserProfile(String name, String bio, String image) {
        sessionManager.updateUserProfile(name, bio, image);
        refreshCurrentUser();
    }

    @Override
    public void refreshContacts() {
        contactsLiveData.setValue(mockDataService.getContacts());
    }

    private void refreshCurrentUser() {
        currentUserLiveData.setValue(sessionManager.getCurrentUser());
    }
}
