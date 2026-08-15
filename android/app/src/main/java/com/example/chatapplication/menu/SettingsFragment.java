package com.example.chatapplication.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.common.ThemeManager;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.FragmentSettingsBinding;
import com.example.chatapplication.view.profile.ProfileActivity;
import com.example.chatapplication.view.startup.SplashScreenActivity;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SessionManager sessionManager;
    private ThemeManager themeManager;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        if (getContext() != null) {
            sessionManager = SessionManager.getInstance(getContext());
            themeManager = ThemeManager.getInstance(getContext());
        }

        loadUserInfo();
        setupThemeChips();
        initClickActions();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo() {
        if (sessionManager == null) return;

        String userName = sessionManager.getUserName();
        String userPhone = sessionManager.getUserPhone();
        String userBio = sessionManager.getUserBio();
        String imageProfile = sessionManager.getUserImage();

        binding.tvUsername.setText(userName);
        binding.tvPhone.setText(userPhone);
        if (userBio != null && !userBio.isEmpty()) {
            binding.tvBio.setText(userBio);
        }

        if (imageProfile != null && !imageProfile.isEmpty() && getContext() != null) {
            Glide.with(this)
                    .load(imageProfile)
                    .placeholder(R.drawable.icon_person)
                    .into(binding.imageProfile);
        }
    }

    private void setupThemeChips() {
        if (themeManager == null) return;
        String currentTheme = themeManager.getCurrentTheme();
        updateThemeChipUI(currentTheme);

        binding.chipThemeLight.setOnClickListener(v -> selectTheme(ThemeManager.THEME_LIGHT_NEU, "Soft Light Neumorphic"));
        binding.chipThemeEmerald.setOnClickListener(v -> selectTheme(ThemeManager.THEME_EMERALD, "Emerald Discreet"));
        binding.chipThemeRoyal.setOnClickListener(v -> selectTheme(ThemeManager.THEME_ROYAL_INDIGO, "Royal Indigo"));
        binding.chipThemeDark.setOnClickListener(v -> selectTheme(ThemeManager.THEME_CYBER_DARK, "Obsidian Dark"));
    }

    private void selectTheme(String themeKey, String displayName) {
        if (themeManager == null || getContext() == null) return;
        themeManager.setTheme(themeKey);
        updateThemeChipUI(themeKey);
        binding.tvActiveThemeName.setText(displayName);
        Toast.makeText(getContext(), displayName + " theme selected", Toast.LENGTH_SHORT).show();
    }

    private void updateThemeChipUI(String activeKey) {
        resetChip(binding.chipThemeLight, "⚪ Soft Light");
        resetChip(binding.chipThemeEmerald, "🟢 Emerald");
        resetChip(binding.chipThemeRoyal, "🟣 Royal");
        resetChip(binding.chipThemeDark, "⚫ Obsidian");

        if (ThemeManager.THEME_EMERALD.equals(activeKey)) {
            setActiveChip(binding.chipThemeEmerald, "🟢 Emerald");
            binding.tvActiveThemeName.setText("Emerald Discreet");
        } else if (ThemeManager.THEME_ROYAL_INDIGO.equals(activeKey)) {
            setActiveChip(binding.chipThemeRoyal, "🟣 Royal");
            binding.tvActiveThemeName.setText("Royal Indigo");
        } else if (ThemeManager.THEME_CYBER_DARK.equals(activeKey)) {
            setActiveChip(binding.chipThemeDark, "⚫ Obsidian");
            binding.tvActiveThemeName.setText("Obsidian Dark");
        } else {
            setActiveChip(binding.chipThemeLight, "⚪ Soft Light");
            binding.tvActiveThemeName.setText("Soft Light Neumorphic");
        }
    }

    private void resetChip(TextView chip, String text) {
        if (getContext() == null) return;
        chip.setBackgroundResource(R.drawable.bg_neu_chip_inactive);
        chip.setTextColor(getResources().getColor(R.color.neu_text_secondary));
        chip.setPadding(dpToPx(14), dpToPx(8), dpToPx(14), dpToPx(8));
        chip.setText(text);
    }

    private void setActiveChip(TextView chip, String text) {
        if (getContext() == null) return;
        chip.setBackgroundResource(R.drawable.bg_neu_chip_active);
        chip.setTextColor(getResources().getColor(R.color.neu_text_on_accent));
        chip.setPadding(dpToPx(14), dpToPx(8), dpToPx(14), dpToPx(8));
        chip.setText(text);
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void initClickActions() {
        // QR Code button
        binding.btnQrCode.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "My Discreet QR Code • Share Profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Profile Hero Card -> Opens ProfileActivity
        binding.cardProfile.setOnClickListener(v -> 
            startActivity(new Intent(getContext(), ProfileActivity.class))
        );

        // Privacy & Security
        binding.rowPrivacy.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Privacy & Security Settings", Toast.LENGTH_SHORT).show();
            }
        });

        // Two-Step Verification
        binding.rowTwoStep.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Two-Step Verification: Active", Toast.LENGTH_SHORT).show();
            }
        });

        // Chat Preferences
        binding.rowChats.setOnClickListener(v -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Chat Wallpapers & History Settings", Toast.LENGTH_SHORT).show();
            }
        });

        // Notifications Switch
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (getContext() != null) {
                String status = isChecked ? "Notifications enabled" : "Notifications muted";
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            }
        });

        // Logout Button
        binding.btnLogout.setOnClickListener(v -> showSignOutDialog());
    }

    private void showSignOutDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out from Discreet on this device?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logout();
                        Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        if (getActivity() != null) getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
