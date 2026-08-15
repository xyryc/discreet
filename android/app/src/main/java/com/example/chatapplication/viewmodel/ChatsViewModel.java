package com.example.chatapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.model.User;
import com.example.chatapplication.repository.ChatRepository;
import com.example.chatapplication.repository.UserRepository;
import com.example.chatapplication.repository.impl.ChatRepositoryImpl;
import com.example.chatapplication.repository.impl.UserRepositoryImpl;

import java.util.List;

public class ChatsViewModel extends AndroidViewModel {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final LiveData<List<ChatItem>> recentChats;
    private final LiveData<List<User>> activeContacts;

    public ChatsViewModel(@NonNull Application application) {
        super(application);
        this.chatRepository = ChatRepositoryImpl.getInstance();
        this.userRepository = UserRepositoryImpl.getInstance(application);
        this.recentChats = chatRepository.getRecentChats();
        this.activeContacts = userRepository.getContacts();
    }

    public LiveData<List<ChatItem>> getRecentChats() {
        return recentChats;
    }

    public LiveData<List<User>> getActiveContacts() {
        return activeContacts;
    }

    public void refresh() {
        chatRepository.refreshChats();
        userRepository.refreshContacts();
    }
}
