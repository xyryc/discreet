package com.example.chatapplication.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivitySetUserInfoBinding;
import com.example.chatapplication.view.MainActivity;

public class SetUserInfoActivity extends AppCompatActivity {

    private ActivitySetUserInfoBinding binding;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);
        sessionManager = SessionManager.getInstance(this);
        progressDialog = new ProgressDialog(this);

        initButtonClick();
    }

    private void initButtonClick() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = binding.edName.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Please input Username", Toast.LENGTH_SHORT).show();
                } else {
                    doUpdate(userName);
                }
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Avatar selection will be available soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doUpdate(final String userName) {
        progressDialog.setMessage("Setting up your profile...");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                sessionManager.updateUserProfile(userName, "Hey there! I am using Discreet.", "");
                Toast.makeText(getApplicationContext(), "Profile setup complete!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SetUserInfoActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 800);
    }
}