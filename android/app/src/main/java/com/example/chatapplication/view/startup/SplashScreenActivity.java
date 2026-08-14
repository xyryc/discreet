package com.example.chatapplication.view.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chatapplication.R;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.view.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = SessionManager.getInstance(SplashScreenActivity.this);
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
                }
                finish();
            }
        }, 1500);
    }
}