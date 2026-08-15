package com.example.chatapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.adapter.ActiveContactsAdapter;
import com.example.chatapplication.adapter.ChatListAdapter;
import com.example.chatapplication.databinding.FragmentChatsBinding;
import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.view.contact.ContactsActivity;
import com.example.chatapplication.viewmodel.ChatsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatsViewModel viewModel;
    private ChatListAdapter chatListAdapter;
    private ActiveContactsAdapter activeContactsAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        setupRecyclers();
        observeViewModel();
        setupSearchFilter();
        initClickActions();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refresh();
        }
    }

    private void setupRecyclers() {
        // Recent chats
        binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
        chatListAdapter = new ChatListAdapter(new ArrayList<>(), getContext());
        binding.recyclerViewChats.setAdapter(chatListAdapter);

        // Active contacts tray
        binding.recyclerViewStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        activeContactsAdapter = new ActiveContactsAdapter(new ArrayList<>(), getContext());
        binding.recyclerViewStories.setAdapter(activeContactsAdapter);
    }

    private void observeViewModel() {
        // Observe Recent Conversations
        viewModel.getRecentChats().observe(getViewLifecycleOwner(), chats -> {
            if (chats != null) {
                chatListAdapter.updateList(chats);
                updateUnreadHeader(chats);
                binding.layoutEmptyState.setVisibility(chats.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        // Observe Active Online Contacts
        viewModel.getActiveContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null) {
                activeContactsAdapter = new ActiveContactsAdapter(contacts, getContext());
                binding.recyclerViewStories.setAdapter(activeContactsAdapter);
            }
        });
    }

    private void updateUnreadHeader(List<ChatItem> chats) {
        int unreadTotal = 0;
        for (ChatItem chat : chats) {
            if (chat.getUnreadCount() > 0) {
                unreadTotal++;
            }
        }

        if (unreadTotal > 0) {
            binding.tvUnreadHeader.setText(unreadTotal + " unread conversations");
            binding.tvUnreadHeader.setVisibility(View.VISIBLE);
        } else {
            binding.tvUnreadHeader.setText("All caught up");
        }
    }

    private void setupSearchFilter() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chatListAdapter != null) {
                    chatListAdapter.filter(s.toString());
                    boolean isEmpty = chatListAdapter.getItemCount() == 0;
                    binding.layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initClickActions() {
        binding.btnHeaderNewChat.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ContactsActivity.class))
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
