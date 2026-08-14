package com.example.chatapplication.view.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityPhoneLoginBinding;

import java.util.UUID;

public class PhoneLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityPhoneLoginBinding binding;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private boolean isCodeSent = false;

    String[] country = {"Bangladesh", "Russia", "Palestine", "China", "Turkey", "Japan", "Iran", "USA", "KSA", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);

        sessionManager = SessionManager.getInstance(this);
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, SetUserInfoActivity.class));
            finish();
            return;
        }

        // Initialize Country Spinner
        Spinner spin = findViewById(R.id.spinner_country);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        progressDialog = new ProgressDialog(this);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCodeSent) {
                    // Send Code Step
                    String phone = binding.edPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(PhoneLoginActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressDialog.setMessage("Sending Verification Code...");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            isCodeSent = true;
                            binding.btnNext.setText("Verify & Continue");
                            binding.edCode.setText("123456"); // Pre-fill mock OTP
                            Toast.makeText(PhoneLoginActivity.this, "OTP sent! Use 123456", Toast.LENGTH_LONG).show();
                        }
                    }, 1000);

                } else {
                    // Verify Code Step
                    String code = binding.edCode.getText().toString().trim();
                    if (TextUtils.isEmpty(code)) {
                        Toast.makeText(PhoneLoginActivity.this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressDialog.setMessage("Verifying...");
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            String fullPhone = "+" + binding.edCodeCountry.getText().toString() + binding.edPhone.getText().toString();
                            String userId = "user_" + UUID.randomUUID().toString().substring(0, 8);
                            String mockJwt = "mock_jwt_token_" + System.currentTimeMillis();

                            sessionManager.createLoginSession(userId, fullPhone, mockJwt);

                            Toast.makeText(PhoneLoginActivity.this, "Phone verified successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
                            finish();
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Country selected
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}