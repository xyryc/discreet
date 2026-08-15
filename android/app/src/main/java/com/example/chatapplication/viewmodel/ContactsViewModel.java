package com.example.chatapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.UserRepository;
import com.example.chatapplication.repository.impl.UserRepositoryImpl;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final LiveData<List<User>> contacts;
    private final String currentUserId;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = UserRepositoryImpl.getInstance(application);
        this.contacts = userRepository.getContacts();
        this.currentUserId = SessionManager.getInstance(application).getUserId();
    }

    public LiveData<List<User>> getContacts() {
        return contacts;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void refresh() {
        userRepository.refreshContacts();
    }
}
