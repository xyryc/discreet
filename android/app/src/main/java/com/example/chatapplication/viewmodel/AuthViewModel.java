package com.example.chatapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.Resource;
import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.AuthRepository;
import com.example.chatapplication.repository.impl.AuthRepositoryImpl;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepositoryImpl(application.getApplicationContext());
    }

    public LiveData<Resource<User>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public LiveData<Resource<User>> register(String name, String email, String password) {
        return authRepository.register(name, email, password);
    }

    public LiveData<Resource<User>> guestLogin() {
        return authRepository.guestLogin();
    }
}
