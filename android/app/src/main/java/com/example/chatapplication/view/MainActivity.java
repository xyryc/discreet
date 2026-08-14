package com.example.chatapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.example.chatapplication.menu.CallsFragment;
import com.example.chatapplication.menu.ChatsFragment;
import com.example.chatapplication.menu.StatusFragment;
import com.example.chatapplication.view.contact.ContactsActivity;
import com.example.chatapplication.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolbar);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabICon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setUpWithViewPager(ViewPager viewPager){
      MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
      adapter.addFragment(new ChatsFragment(), "Chats");
      adapter.addFragment(new StatusFragment(), "Status");
      adapter.addFragment(new CallsFragment(), "Calls");
      viewPager.setAdapter(adapter);
    }

    //add this code
    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {super(manager) ;}

        @Override
        public Fragment getItem(int position){return mFragmentList.get(position);}

        @Override
        public  int getCount(){return mFragmentList.size();}

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {return mFragmentTitleList.get(position);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        // Using if-else instead of switch-case because Resource IDs are non-final in AGP 8+
        if (id == R.id.menu_search) {
            // Open contact search screen
            Toast.makeText(MainActivity.this, "Action Search", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            return true;
        } else if (id == R.id.action_new_group) {
            // Create a new group chat
            Toast.makeText(MainActivity.this, "Action New Group", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_new_broadcast) {
            // Create a new broadcast list
            Toast.makeText(MainActivity.this, "Action Broadcast", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_royalchat_web) {
            // Link web client / QR code sync
            Toast.makeText(MainActivity.this, "Action Web", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_started_message) {
            // View starred / bookmarked messages
            Toast.makeText(MainActivity.this, "Action Starred Message", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_settings) {
            // Navigate to user settings screen
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeFabICon(final int index){
        binding.fabAction.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch(index){
                    case 0 : binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_chat_24));
                                binding.fabAction.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                                    }
                                });

                    break;
                    case 1 : binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_camera_24));
                    break;
                    case 2 : binding.fabAction.setImageDrawable(getDrawable(R.drawable.ic_baseline_call_24)); break;
                }
                binding.fabAction.show();
            }
        },400);
    }
}