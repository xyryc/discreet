package com.example.chatapplication.menu;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.adapter.ActiveContactsAdapter;
import com.example.chatapplication.adapter.ChatListAdapter;
import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.databinding.FragmentChatsBinding;
import com.example.chatapplication.model.ChatList;
import com.example.chatapplication.model.user.Users;
import com.example.chatapplication.view.contact.ContactsActivity;

import java.util.List;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatListAdapter chatListAdapter;
    private ActiveContactsAdapter activeContactsAdapter;
    private MockDataService mockDataService;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        mockDataService = MockDataService.getInstance();

        setupChatsRecycler();
        setupActiveContactsRecycler();
        setupSearchFilter();
        initClickActions();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshChats();
    }

    private void setupChatsRecycler() {
        binding.recyclerViewChats.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ChatList> chats = mockDataService.getRecentChats();
        chatListAdapter = new ChatListAdapter(chats, getContext());
        binding.recyclerViewChats.setAdapter(chatListAdapter);

        updateUnreadHeader(chats);
    }

    private void setupActiveContactsRecycler() {
        binding.recyclerViewStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Users> contacts = mockDataService.getContacts();
        activeContactsAdapter = new ActiveContactsAdapter(contacts, getContext());
        binding.recyclerViewStories.setAdapter(activeContactsAdapter);
    }

    private void updateUnreadHeader(List<ChatList> chats) {
        int unreadTotal = 0;
        for (ChatList chat : chats) {
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

    private void refreshChats() {
        if (chatListAdapter != null) {
            List<ChatList> chats = mockDataService.getRecentChats();
            chatListAdapter.updateList(chats);
            updateUnreadHeader(chats);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}