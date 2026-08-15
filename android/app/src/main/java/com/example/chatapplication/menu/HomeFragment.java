package com.example.chatapplication.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.FragmentHomeBinding;
import com.example.chatapplication.view.contact.ContactsActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private SessionManager sessionManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        if (getContext() != null) {
            sessionManager = SessionManager.getInstance(getContext());
        }

        updateGreeting();
        initClickActions();

        return binding.getRoot();
    }

    private void updateGreeting() {
        if (sessionManager != null) {
            String name = sessionManager.getUserName();
            if (name != null && !name.isEmpty()) {
                binding.tvGreeting.setText("Hi, " + name);
            }
        }
    }

    private void initClickActions() {
        binding.btnActionChat.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );

        binding.btnActionContacts.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );

        binding.btnActionCall.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );

        binding.btnSearch.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
