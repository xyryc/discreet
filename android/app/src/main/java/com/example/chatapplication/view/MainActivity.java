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
    public boolean onOptionsItemSelected(MenuItem item){
        //handle actionbar item click here. The actionbar will
        //automatically handle clicks on the Home/Up button, so
        //long as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();

        switch (id){
            case R.id.menu_search : Toast.makeText(MainActivity.this, "Action Search", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                break;
            case R.id.action_new_group: Toast.makeText(MainActivity.this, "Action New Group", Toast.LENGTH_LONG).show(); break;
            case R.id.action_new_broadcast: Toast.makeText(MainActivity.this, "Action Broadcast", Toast.LENGTH_LONG).show(); break;
            case R.id.action_royalchat_web: Toast.makeText(MainActivity.this, "Action Web", Toast.LENGTH_LONG).show(); break;
            case R.id.action_started_message: Toast.makeText(MainActivity.this, "Action Starred Message", Toast.LENGTH_LONG).show(); break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
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