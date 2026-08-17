package com.example.chatapplication.repository.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.model.Resource;
import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.AuthRepository;

import java.util.UUID;

public class AuthRepositoryImpl implements AuthRepository {

    private final SessionManager sessionManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AuthRepositoryImpl(Context context) {
        this.sessionManager = SessionManager.getInstance(context);
    }

    @Override
    public LiveData<Resource<User>> login(String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        mainHandler.postDelayed(() -> {
            if (email == null || !email.contains("@")) {
                result.setValue(Resource.error("Please enter a valid email address."));
                return;
            }
            if (password == null || password.length() < 6) {
                result.setValue(Resource.error("Password must be at least 6 characters."));
                return;
            }

            // Derive display name from email (e.g. alex@example.com -> Alex)
            String namePart = email.split("@")[0];
            String displayName = namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
            String userId = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String mockToken = "discreet_jwt_" + System.currentTimeMillis();

            sessionManager.createLoginSession(userId, email, mockToken);
            sessionManager.updateUserProfile(displayName, "Encrypted & Discreet 🛡️", "");

            User user = sessionManager.getCurrentUser();
            result.setValue(Resource.success(user));
        }, 750);

        return result;
    }

    @Override
    public LiveData<Resource<User>> register(String name, String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        mainHandler.postDelayed(() -> {
            if (name == null || name.trim().isEmpty()) {
                result.setValue(Resource.error("Please enter your display name."));
                return;
            }
            if (email == null || !email.contains("@")) {
                result.setValue(Resource.error("Please enter a valid email address."));
                return;
            }
            if (password == null || password.length() < 6) {
                result.setValue(Resource.error("Password must be at least 6 characters."));
                return;
            }

            String userId = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String mockToken = "discreet_jwt_" + System.currentTimeMillis();

            sessionManager.createLoginSession(userId, email, mockToken);
            sessionManager.updateUserProfile(name.trim(), "Available on Discreet 🔒", "");

            User user = sessionManager.getCurrentUser();
            result.setValue(Resource.success(user));
        }, 850);

        return result;
    }

    @Override
    public LiveData<Resource<User>> guestLogin() {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        mainHandler.postDelayed(() -> {
            String randomHex = UUID.randomUUID().toString().substring(0, 4);
            String guestName = "Guest " + randomHex;
            String guestHandle = "@discreet/anon_" + randomHex;
            String userId = "guest_" + UUID.randomUUID().toString().substring(0, 8);
            String mockToken = "guest_jwt_" + System.currentTimeMillis();

            sessionManager.createLoginSession(userId, guestHandle, mockToken);
            sessionManager.updateUserProfile(guestName, "Anonymous Persona 🕶️", "");

            User user = sessionManager.getCurrentUser();
            result.setValue(Resource.success(user));
        }, 500);

        return result;
    }
}
