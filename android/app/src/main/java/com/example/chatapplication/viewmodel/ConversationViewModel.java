package com.example.chatapplication.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.model.ChatMessage;
import com.example.chatapplication.repository.ChatRepository;
import com.example.chatapplication.repository.impl.ChatRepositoryImpl;

import java.util.List;

public class ConversationViewModel extends AndroidViewModel {

    private final ChatRepository chatRepository;
    private final String currentUserId;

    public ConversationViewModel(@NonNull Application application) {
        super(application);
        this.chatRepository = ChatRepositoryImpl.getInstance();
        this.currentUserId = SessionManager.getInstance(application).getUserId();
    }

    public LiveData<List<ChatMessage>> getMessages(String receiverId) {
        return chatRepository.getMessages(receiverId);
    }

    public void sendMessage(String receiverId, String text) {
        chatRepository.sendMessage(currentUserId, receiverId, text);

        // Simulate incoming response after 1.2s for seamless mock experience
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String reply = "Got your message: \"" + text + "\" 👍";
            chatRepository.sendMessage(receiverId, currentUserId, reply);
        }, 1200);
    }

    public String getCurrentUserId() {
        return currentUserId;
    }
}
