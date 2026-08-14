package com.example.chatapplication.menu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.CallListAdapter;
import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.model.CallList;

import java.util.List;

public class CallsFragment extends Fragment {

    private RecyclerView recyclerView;

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCalls();
        return view;
    }

    private void loadCalls() {
        List<CallList> lists = MockDataService.getInstance().getCallHistory();
        recyclerView.setAdapter(new CallListAdapter(lists, getContext()));
    }
}