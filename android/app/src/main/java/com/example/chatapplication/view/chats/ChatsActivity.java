package com.example.chatapplication.view.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ChatsAdapter;
import com.example.chatapplication.databinding.ActivityChatsBinding;
import com.example.chatapplication.model.ChatMessage;
import com.example.chatapplication.viewmodel.ConversationViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private ConversationViewModel viewModel;
    private String receiverID;
    private String receiverName;
    private ChatsAdapter adapter;
    private final List<ChatMessage> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);
        viewModel = new ViewModelProvider(this).get(ConversationViewModel.class);

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

        initRecycler();
        initSendAction();
        observeMessages();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatsAdapter(messageList, this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void initSendAction() {
        binding.btnSend.setOnClickListener(v -> {
            String messageText = binding.edMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                viewModel.sendMessage(receiverID, messageText);
                binding.edMessage.setText("");
            }
        });
    }

    private void observeMessages() {
        viewModel.getMessages(receiverID).observe(this, messages -> {
            if (messages != null) {
                messageList.clear();
                messageList.addAll(messages);
                adapter.notifyDataSetChanged();
                if (messageList.size() > 0) {
                    binding.recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }
        });
    }
}