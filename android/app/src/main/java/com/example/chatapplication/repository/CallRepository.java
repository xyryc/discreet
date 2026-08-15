package com.example.chatapplication.repository;

import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.CallItem;

import java.util.List;

public interface CallRepository {
    LiveData<List<CallItem>> getCallHistory();
    void refreshCalls();
}
