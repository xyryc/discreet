package com.example.chatapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.model.ChatMessage;

import java.util.List;

public interface ChatRepository {
    LiveData<List<ChatItem>> getRecentChats();
    LiveData<List<ChatMessage>> getMessages(String receiverId);
    void sendMessage(String senderId, String receiverId, String text);
    void refreshChats();
}
