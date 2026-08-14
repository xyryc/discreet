package com.example.chatapplication.view.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ChatsAdapter;
import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityChatsBinding;
import com.example.chatapplication.model.chat.Chats;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private String receiverID;
    private ChatsAdapter adapter;
    private List<Chats> list;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        currentUserId = SessionManager.getInstance(this).getUserId();

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");

        if (receiverID == null) {
            receiverID = "user_1";
        }

        // Showing selected user's name and image
        if (userName != null) {
            binding.tvUsername.setText(userName);
        }
        if (userProfile != null && !userProfile.isEmpty()) {
            Glide.with(this).load(userProfile).into(binding.imageProfile);
        }

        // Back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        readChats();
    }

    private void initBtnClick() {
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = binding.edMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(messageText)) {
                    sendTextMessage(messageText);
                    binding.edMessage.setText("");
                }
            }
        });
    }

    private void sendTextMessage(String text) {
        MockDataService.getInstance().sendMessage(currentUserId, receiverID, text);
        readChats();
    }

    private void readChats() {
        List<Chats> messages = MockDataService.getInstance().getMessages(receiverID);
        list.clear();
        list.addAll(messages);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ChatsAdapter(list, ChatsActivity.this);
            binding.recyclerView.setAdapter(adapter);
        }

        if (list.size() > 0) {
            binding.recyclerView.scrollToPosition(list.size() - 1);
        }
    }
}