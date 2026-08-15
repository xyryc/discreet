package com.example.chatapplication.data;

import com.example.chatapplication.model.CallItem;
import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.model.ChatMessage;
import com.example.chatapplication.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MockDataService {
    private static MockDataService instance;
    private final List<User> mockUsers = new ArrayList<>();
    private final List<ChatItem> mockChatLists = new ArrayList<>();
    private final List<CallItem> mockCallLists = new ArrayList<>();
    private final Map<String, List<ChatMessage>> mockConversationMap = new HashMap<>();

    private MockDataService() {
        initMockData();
    }

    public static synchronized MockDataService getInstance() {
        if (instance == null) {
            instance = new MockDataService();
        }
        return instance;
    }

    private void initMockData() {
        // Mock Contacts / Users
        mockUsers.add(new User("user_1", "Alex Morgan", "+1 (555) 019-2834", 
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", 
                "", "alex@example.com", "12/05/1996", "Female", "Available", "Building something awesome 🚀"));

        mockUsers.add(new User("user_2", "David Kim", "+1 (555) 012-9843", 
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", 
                "", "david@example.com", "08/11/1994", "Male", "Busy", "Coding in Java & Spring Boot"));

        mockUsers.add(new User("user_3", "Sarah Connor", "+1 (555) 018-4721", 
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150", 
                "", "sarah@example.com", "23/02/1998", "Female", "At Work", "Discreet & Private"));

        mockUsers.add(new User("user_4", "Michael Scott", "+1 (555) 014-7729", 
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", 
                "", "michael@example.com", "15/03/1985", "Male", "Online", "World's Best Boss"));

        mockUsers.add(new User("user_5", "Elena Rostova", "+1 (555) 017-3819", 
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", 
                "", "elena@example.com", "19/07/1999", "Female", "Online", "Security researcher"));

        // Mock Recent Chat List with rich details
        mockChatLists.add(new ChatItem("user_1", "Alex Morgan", "Are we meeting today for the design review?", "10:30 AM", 
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", 2, true, false));
        
        mockChatLists.add(new ChatItem("user_2", "David Kim", "The Spring Boot WebSocket backend is ready!", "Yesterday", 
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", 0, true, true));

        mockChatLists.add(new ChatItem("user_5", "Elena Rostova", "Verified the end-to-end key exchange protocol 🔒", "Yesterday", 
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", 1, true, false));

        mockChatLists.add(new ChatItem("user_3", "Sarah Connor", "See you at the office tomorrow.", "12/08/2026", 
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150", 0, false, true));

        mockChatLists.add(new ChatItem("user_4", "Michael Scott", "That's what she said! 😂", "10/08/2026", 
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", 0, false, true));

        // Mock Call List with audio/video and durations
        mockCallLists.add(new CallItem("user_1", "Alex Morgan", "Today, 10:15 AM", 
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150", "INCOMING", false, "12m 45s"));

        mockCallLists.add(new CallItem("user_5", "Elena Rostova", "Today, 9:02 AM", 
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150", "MISSED", true, "Missed"));

        mockCallLists.add(new CallItem("user_2", "David Kim", "Yesterday, 6:45 PM", 
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150", "OUTGOING", true, "24m 10s"));

        mockCallLists.add(new CallItem("user_3", "Sarah Connor", "Aug 12, 4:20 PM", 
                "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150", "INCOMING", false, "5m 12s"));

        mockCallLists.add(new CallItem("user_4", "Michael Scott", "Aug 10, 1:15 PM", 
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150", "MISSED", false, "Missed"));

        // Mock Message Thread for user_1
        List<ChatMessage> user1Messages = new ArrayList<>();
        user1Messages.add(new ChatMessage("10:15 AM", "Hey! How is the Discreet app coming along?", "TEXT", "user_1", "user_me"));
        user1Messages.add(new ChatMessage("10:16 AM", "It is going great! Removed Firebase and building custom Neumorphic UI.", "TEXT", "user_me", "user_1"));
        user1Messages.add(new ChatMessage("10:28 AM", "The soft UI cards look amazing on phone.", "TEXT", "user_1", "user_me"));
        user1Messages.add(new ChatMessage("10:30 AM", "Are we meeting today for the design review?", "TEXT", "user_1", "user_me"));
        mockConversationMap.put("user_1", user1Messages);

        // Mock Message Thread for user_2
        List<ChatMessage> user2Messages = new ArrayList<>();
        user2Messages.add(new ChatMessage("Yesterday, 4:00 PM", "Spring Boot architecture looks super clean.", "TEXT", "user_2", "user_me"));
        user2Messages.add(new ChatMessage("Yesterday, 4:02 PM", "The Spring Boot WebSocket backend is ready!", "TEXT", "user_2", "user_me"));
        mockConversationMap.put("user_2", user2Messages);

        // Mock Message Thread for user_5
        List<ChatMessage> user5Messages = new ArrayList<>();
        user5Messages.add(new ChatMessage("Yesterday, 8:10 PM", "Testing the zero-knowledge session storage.", "TEXT", "user_5", "user_me"));
        user5Messages.add(new ChatMessage("Yesterday, 8:12 PM", "Verified the end-to-end key exchange protocol 🔒", "TEXT", "user_5", "user_me"));
        mockConversationMap.put("user_5", user5Messages);
    }

    public List<User> getContacts() {
        return new ArrayList<>(mockUsers);
    }

    public List<ChatItem> getRecentChats() {
        return new ArrayList<>(mockChatLists);
    }

    public List<CallItem> getCallHistory() {
        return new ArrayList<>(mockCallLists);
    }

    public List<ChatMessage> getMessages(String receiverId) {
        List<ChatMessage> messages = mockConversationMap.get(receiverId);
        if (messages == null) {
            messages = new ArrayList<>();
            mockConversationMap.put(receiverId, messages);
        }
        return messages;
    }

    public void sendMessage(String senderId, String receiverId, String text) {
        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
        ChatMessage chat = new ChatMessage(currentTime, text, "TEXT", senderId, receiverId);
        getMessages(receiverId).add(chat);

        // Update last message in chat list
        for (ChatItem item : mockChatLists) {
            if (item.getUserID().equals(receiverId)) {
                item.setDescription(text);
                item.setDate(currentTime);
                break;
            }
        }
    }
}
