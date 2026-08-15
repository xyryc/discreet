package com.example.chatapplication.model;

public class ChatList {
    private String userID;
    private String userName;
    private String description;
    private String date;
    private String urlProfile;
    private int unreadCount;
    private boolean isOnline;
    private boolean isRead;

    public ChatList() {
    }

    public ChatList(String userID, String userName, String description, String date, String urlProfile) {
        this.userID = userID;
        this.userName = userName;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
        this.unreadCount = 0;
        this.isOnline = false;
        this.isRead = true;
    }

    public ChatList(String userID, String userName, String description, String date, String urlProfile, int unreadCount, boolean isOnline, boolean isRead) {
        this.userID = userID;
        this.userName = userName;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
        this.unreadCount = unreadCount;
        this.isOnline = isOnline;
        this.isRead = isRead;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
