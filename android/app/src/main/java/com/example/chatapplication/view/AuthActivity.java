package com.example.chatapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityAuthBinding;
import com.example.chatapplication.model.Resource;
import com.example.chatapplication.model.User;
import com.example.chatapplication.viewmodel.AuthViewModel;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;
    private boolean isRegisterMode = false;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupTabs();
        setupPasswordToggle();
        setupActions();
    }

    private void setupTabs() {
        binding.tabSignIn.setOnClickListener(v -> setAuthMode(false));
        binding.tabRegister.setOnClickListener(v -> setAuthMode(true));
    }

    private void setAuthMode(boolean register) {
        isRegisterMode = register;
        if (isRegisterMode) {
            binding.tabSignIn.setBackground(null);
            binding.tvTabSignIn.setTextColor(getColor(R.color.neu_text_secondary));

            binding.tabRegister.setBackgroundResource(R.drawable.bg_neu_chip_active);
            binding.tvTabRegister.setTextColor(getColor(R.color.neu_text_on_accent));

            binding.tvAuthTitle.setText("Create Account");
            binding.tvAuthSubtitle.setText("Join Discreet with end-to-end encryption");
            binding.layoutNameInput.setVisibility(View.VISIBLE);
            binding.tvBtnSubmit.setText("Create Encrypted Account");
        } else {
            binding.tabRegister.setBackground(null);
            binding.tvTabRegister.setTextColor(getColor(R.color.neu_text_secondary));

            binding.tabSignIn.setBackgroundResource(R.drawable.bg_neu_chip_active);
            binding.tvTabSignIn.setTextColor(getColor(R.color.neu_text_on_accent));

            binding.tvAuthTitle.setText("Welcome Back");
            binding.tvAuthSubtitle.setText("Sign in to access your encrypted conversations");
            binding.layoutNameInput.setVisibility(View.GONE);
            binding.tvBtnSubmit.setText("Sign In to Discreet");
        }
    }

    private void setupPasswordToggle() {
        binding.btnTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.btnTogglePassword.setImageResource(R.drawable.ic_baseline_visibility_off_24);
            } else {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.btnTogglePassword.setImageResource(R.drawable.ic_baseline_visibility_24);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
    }

    private void setupActions() {
        binding.btnSubmit.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (isRegisterMode) {
                String name = binding.etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "Please enter your display name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                authViewModel.register(name, email, password).observe(this, this::handleAuthResult);
            } else {
                if (TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                authViewModel.login(email, password).observe(this, this::handleAuthResult);
            }
        });

        binding.btnGuestLogin.setOnClickListener(v -> 
            authViewModel.guestLogin().observe(this, this::handleAuthResult)
        );
    }

    private void handleAuthResult(Resource<User> resource) {
        if (resource == null) return;

        switch (resource.getStatus()) {
            case LOADING:
                binding.layoutLoading.setVisibility(View.VISIBLE);
                binding.btnSubmit.setEnabled(false);
                break;

            case SUCCESS:
                binding.layoutLoading.setVisibility(View.GONE);
                binding.btnSubmit.setEnabled(true);
                User user = resource.getData();
                String name = (user != null && user.getUserName() != null) ? user.getUserName() : "User";
                Toast.makeText(this, "Welcome to Discreet, " + name + "!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

            case ERROR:
                binding.layoutLoading.setVisibility(View.GONE);
                binding.btnSubmit.setEnabled(true);
                String msg = resource.getMessage() != null ? resource.getMessage() : "Authentication failed.";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
