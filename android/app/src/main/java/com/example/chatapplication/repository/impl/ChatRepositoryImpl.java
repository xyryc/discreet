package com.example.chatapplication.repository.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.model.ChatMessage;
import com.example.chatapplication.repository.ChatRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepositoryImpl implements ChatRepository {

    private static ChatRepositoryImpl instance;
    private final MockDataService mockDataService;
    private final MutableLiveData<List<ChatItem>> recentChatsLiveData = new MutableLiveData<>();
    private final Map<String, MutableLiveData<List<ChatMessage>>> messageCache = new HashMap<>();

    private ChatRepositoryImpl() {
        this.mockDataService = MockDataService.getInstance();
        refreshChats();
    }

    public static synchronized ChatRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ChatRepositoryImpl();
        }
        return instance;
    }

    @Override
    public LiveData<List<ChatItem>> getRecentChats() {
        refreshChats();
        return recentChatsLiveData;
    }

    @Override
    public LiveData<List<ChatMessage>> getMessages(String receiverId) {
        MutableLiveData<List<ChatMessage>> liveData = messageCache.get(receiverId);
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            messageCache.put(receiverId, liveData);
        }
        liveData.setValue(mockDataService.getMessages(receiverId));
        return liveData;
    }

    @Override
    public void sendMessage(String senderId, String receiverId, String text) {
        mockDataService.sendMessage(senderId, receiverId, text);
        // Notify observers of updated message thread and recent chats
        if (messageCache.containsKey(receiverId)) {
            messageCache.get(receiverId).setValue(mockDataService.getMessages(receiverId));
        }
        refreshChats();
    }

    @Override
    public void refreshChats() {
        recentChatsLiveData.setValue(mockDataService.getRecentChats());
    }
}
