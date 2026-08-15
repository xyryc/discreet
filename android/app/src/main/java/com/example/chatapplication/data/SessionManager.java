package com.example.chatapplication.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chatapplication.model.User;

public class SessionManager {
    private static final String PREF_NAME = "discreet_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_BIO = "user_bio";
    private static final String KEY_USER_IMAGE = "user_image";
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void createLoginSession(String userId, String phone, String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.apply();
    }

    public void updateUserProfile(String userName, String bio, String imageProfile) {
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_BIO, bio);
        editor.putString(KEY_USER_IMAGE, imageProfile);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "user_me");
    }

    public String getUserPhone() {
        return pref.getString(KEY_USER_PHONE, "+1 (555) 019-2834");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "My Profile");
    }

    public String getUserBio() {
        return pref.getString(KEY_USER_BIO, "Hey there! I am using Discreet.");
    }

    public String getUserImage() {
        return pref.getString(KEY_USER_IMAGE, "");
    }

    public String getAuthToken() {
        return pref.getString(KEY_AUTH_TOKEN, "");
    }

    public User getCurrentUser() {
        User user = new User();
        user.setUserID(getUserId());
        user.setUserName(getUserName());
        user.setUserPhone(getUserPhone());
        user.setBio(getUserBio());
        user.setImageProfile(getUserImage());
        user.setStatus("Online");
        return user;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
