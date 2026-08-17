package com.example.chatapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.OnboardingAdapter;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityOnboardingBinding;
import com.example.chatapplication.model.OnboardingSlide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private SessionManager sessionManager;
    private final List<OnboardingSlide> slides = new ArrayList<>();
    private String generatedHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);
        sessionManager = SessionManager.getInstance(this);

        String randomHex = UUID.randomUUID().toString().substring(0, 4);
        generatedHandle = "@discreet/user_" + randomHex;

        setupSlides();
        setupViewPager();
        setupClickActions();
    }

    private void setupSlides() {
        slides.add(new OnboardingSlide(
                R.drawable.ic_discreet_logo,
                "Zero-Trace Privacy",
                "End-to-end encrypted messaging with zero metadata tracking and self-healing session keys.",
                "ZERO-KNOWLEDGE"
        ));

        slides.add(new OnboardingSlide(
                R.drawable.ic_baseline_security_24,
                "Public Shield Mode",
                "Anti-shoulder surfing protection. Tap-and-hold to optically reveal messages in public spaces.",
                "ANTI-SNOOPING"
        ));

        slides.add(new OnboardingSlide(
                R.drawable.ic_baseline_vpn_key_24,
                "Burner Ephemeral Rooms",
                "Generate disposable, single-use chat rooms that self-destruct with zero digital footprints.",
                "EPHEMERAL CHATS"
        ));
    }

    private void setupViewPager() {
        OnboardingAdapter adapter = new OnboardingAdapter(slides);
        binding.viewPagerOnboarding.setAdapter(adapter);

        binding.viewPagerOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicators(position);

                if (position == slides.size() - 1) {
                    binding.tvBtnNext.setText("Get Started");
                } else {
                    binding.tvBtnNext.setText("Continue");
                }
            }
        });
    }

    private void updateIndicators(int position) {
        setDot(binding.dot0, position == 0);
        setDot(binding.dot1, position == 1);
        setDot(binding.dot2, position == 2);
    }

    private void setDot(View dot, boolean isActive) {
        if (isActive) {
            dot.setBackgroundResource(R.drawable.bg_neu_chip_active);
            dot.getLayoutParams().width = dpToPx(24);
        } else {
            dot.setBackgroundResource(R.drawable.bg_neu_chip_inactive);
            dot.getLayoutParams().width = dpToPx(8);
        }
        dot.requestLayout();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupClickActions() {
        binding.btnSkip.setOnClickListener(v -> openAuthScreen());

        binding.btnNext.setOnClickListener(v -> {
            int current = binding.viewPagerOnboarding.getCurrentItem();
            if (current < slides.size() - 1) {
                binding.viewPagerOnboarding.setCurrentItem(current + 1, true);
            } else {
                openAuthScreen();
            }
        });
    }

    private void openAuthScreen() {
        Intent intent = new Intent(OnboardingActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void showPersonaDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_create_persona, null);
        dialog.setContentView(sheetView);

        EditText etName = sheetView.findViewById(R.id.et_persona_name);
        TextView tvHandle = sheetView.findViewById(R.id.tv_crypto_handle);
        View btnEnter = sheetView.findViewById(R.id.btn_enter_discreet);

        tvHandle.setText(generatedHandle);

        btnEnter.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                name = "Discreet User";
            }

            String userId = "user_" + UUID.randomUUID().toString().substring(0, 8);
            String mockToken = "discreet_jwt_" + System.currentTimeMillis();

            sessionManager.createLoginSession(userId, generatedHandle, mockToken);
            sessionManager.updateUserProfile(name, "Available on Discreet 🔒", "");

            Toast.makeText(OnboardingActivity.this, "Welcome to Discreet, " + name + "!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }
}
