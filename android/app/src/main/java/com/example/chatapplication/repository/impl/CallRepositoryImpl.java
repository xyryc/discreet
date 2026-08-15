package com.example.chatapplication.repository.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.model.CallItem;
import com.example.chatapplication.repository.CallRepository;

import java.util.List;

public class CallRepositoryImpl implements CallRepository {

    private static CallRepositoryImpl instance;
    private final MockDataService mockDataService;
    private final MutableLiveData<List<CallItem>> callHistoryLiveData = new MutableLiveData<>();

    private CallRepositoryImpl() {
        this.mockDataService = MockDataService.getInstance();
        refreshCalls();
    }

    public static synchronized CallRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new CallRepositoryImpl();
        }
        return instance;
    }

    @Override
    public LiveData<List<CallItem>> getCallHistory() {
        refreshCalls();
        return callHistoryLiveData;
    }

    @Override
    public void refreshCalls() {
        callHistoryLiveData.setValue(mockDataService.getCallHistory());
    }
}
