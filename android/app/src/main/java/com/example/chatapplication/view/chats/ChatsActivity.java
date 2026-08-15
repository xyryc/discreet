package com.example.chatapplication.view.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
    private String receiverName;
    private ChatsAdapter adapter;
    private List<Chats> list;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        currentUserId = SessionManager.getInstance(this).getUserId();

        Intent intent = getIntent();
        receiverName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");

        if (receiverID == null) {
            receiverID = "user_1";
        }
        if (receiverName == null || receiverName.isEmpty()) {
            receiverName = "Alex Morgan";
        }

        binding.tvUsername.setText(receiverName);

        if (userProfile != null && !userProfile.isEmpty()) {
            Glide.with(this).load(userProfile).into(binding.imageProfile);
        }

        binding.btnBack.setOnClickListener(v -> finish());

        // Header call actions
        binding.btnAudioCall.setOnClickListener(v ->
                Toast.makeText(this, "Starting Encrypted Voice Call with " + receiverName + "...", Toast.LENGTH_SHORT).show()
        );

        binding.btnVideoCall.setOnClickListener(v ->
                Toast.makeText(this, "Starting Encrypted HD Video Call with " + receiverName + "...", Toast.LENGTH_SHORT).show()
        );

        binding.btnFile.setOnClickListener(v ->
                Toast.makeText(this, "Select Encrypted Attachment / Media", Toast.LENGTH_SHORT).show()
        );

        binding.btnEmoji.setOnClickListener(v ->
                Toast.makeText(this, "Emoji picker", Toast.LENGTH_SHORT).show()
        );

        initBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        readChats();
    }

    private void initBtnClick() {
        binding.btnSend.setOnClickListener(v -> {
            String messageText = binding.edMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                sendTextMessage(messageText);
                binding.edMessage.setText("");
            }
        });
    }

    private void sendTextMessage(String text) {
        MockDataService.getInstance().sendMessage(currentUserId, receiverID, text);
        readChats();

        // Simulate intelligent live reply after 1.2 seconds for realistic interaction
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isFinishing() && !isDestroyed()) {
                String reply = "Got your message: \"" + text + "\" 👍";
                MockDataService.getInstance().sendMessage(receiverID, currentUserId, reply);
                readChats();
            }
        }, 1200);
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
            binding.recyclerView.smoothScrollToPosition(list.size() - 1);
        }
    }
}