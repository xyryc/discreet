package com.example.chatapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chatapplication.model.CallItem;
import com.example.chatapplication.repository.CallRepository;
import com.example.chatapplication.repository.impl.CallRepositoryImpl;

import java.util.List;

public class CallsViewModel extends AndroidViewModel {

    private final CallRepository callRepository;
    private final LiveData<List<CallItem>> callHistory;

    public CallsViewModel(@NonNull Application application) {
        super(application);
        this.callRepository = CallRepositoryImpl.getInstance();
        this.callHistory = callRepository.getCallHistory();
    }

    public LiveData<List<CallItem>> getCallHistory() {
        return callHistory;
    }

    public void refresh() {
        callRepository.refreshCalls();
    }
}
