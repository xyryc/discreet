package com.example.chatapplication.view.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivitySettingsBinding;
import com.example.chatapplication.view.profile.ProfileActivity;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        sessionManager = SessionManager.getInstance(this);
        loadUserInfo();
        initClickAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void initClickAction() {
        binding.InProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });
    }

    private void loadUserInfo() {
        String userName = sessionManager.getUserName();
        String imageProfile = sessionManager.getUserImage();

        binding.tvUsername.setText(userName);
        if (imageProfile != null && !imageProfile.isEmpty()) {
            Glide.with(SettingsActivity.this).load(imageProfile).into(binding.imageProfile);
        }
    }
}
