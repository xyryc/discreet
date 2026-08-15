package com.example.chatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.example.chatapplication.menu.CallsFragment;
import com.example.chatapplication.menu.ChatsFragment;
import com.example.chatapplication.menu.HomeFragment;
import com.example.chatapplication.menu.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int TAB_HOME = 0;
    private static final int TAB_CALLS = 1;
    private static final int TAB_CHATS = 2;
    private static final int TAB_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupViewPager();
        setupBottomTabs();
    }

    private void setupViewPager() {
        MainTabsAdapter adapter = new MainTabsAdapter(this);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.setUserInputEnabled(true); // Smooth swiping between tabs

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateBottomTabUI(position);
            }
        });
    }

    private void setupBottomTabs() {
        binding.tabHome.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_HOME, true));
        binding.tabCalls.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_CALLS, true));
        binding.tabChats.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_CHATS, true));
        binding.tabSettings.setOnClickListener(v -> binding.viewPager.setCurrentItem(TAB_SETTINGS, true));
    }

    private void updateBottomTabUI(int selectedPosition) {
        // Reset all 4 tabs
        resetTab(binding.tabHome, binding.iconHome, binding.labelHome);
        resetTab(binding.tabCalls, binding.iconCalls, binding.labelCalls);
        resetTab(binding.tabChats, binding.iconChats, binding.labelChats);
        resetTab(binding.tabSettings, binding.iconSettings, binding.labelSettings);

        // Highlight selected tab
        switch (selectedPosition) {
            case TAB_HOME:
                activateTab(binding.tabHome, binding.iconHome, binding.labelHome);
                break;
            case TAB_CALLS:
                activateTab(binding.tabCalls, binding.iconCalls, binding.labelCalls);
                break;
            case TAB_CHATS:
                activateTab(binding.tabChats, binding.iconChats, binding.labelChats);
                break;
            case TAB_SETTINGS:
                activateTab(binding.tabSettings, binding.iconSettings, binding.labelSettings);
                break;
        }
    }

    private void resetTab(LinearLayout tabLayout, ImageView icon, TextView label) {
        tabLayout.setBackground(null);
        icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.neu_text_secondary)));
        label.setTextColor(ContextCompat.getColor(this, R.color.neu_text_secondary));
    }

    private void activateTab(LinearLayout tabLayout, ImageView icon, TextView label) {
        tabLayout.setBackgroundResource(R.drawable.bg_neu_tab_active);
        icon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.neu_accent)));
        label.setTextColor(ContextCompat.getColor(this, R.color.neu_accent));
    }

    private static class MainTabsAdapter extends FragmentStateAdapter {

        public MainTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case TAB_HOME:
                    return new HomeFragment();
                case TAB_CALLS:
                    return new CallsFragment();
                case TAB_CHATS:
                    return new ChatsFragment();
                case TAB_SETTINGS:
                    return new SettingsFragment();
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}