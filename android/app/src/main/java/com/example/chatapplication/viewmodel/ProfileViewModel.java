package com.example.chatapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.UserRepository;
import com.example.chatapplication.repository.impl.UserRepositoryImpl;

public class ProfileViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final LiveData<User> currentUser;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = UserRepositoryImpl.getInstance(application);
        this.currentUser = userRepository.getCurrentUser();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void updateProfile(String name, String bio, String image) {
        userRepository.updateUserProfile(name, bio, image);
    }
}
