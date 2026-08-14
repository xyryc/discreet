package com.example.chatapplication.view.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ContactsAdapter;
import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityContactsBinding;
import com.example.chatapplication.model.user.Users;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;
    private final List<Users> list = new ArrayList<>();
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getContactList();
    }

    private void getContactList() {
        String currentUserId = SessionManager.getInstance(this).getUserId();
        List<Users> allUsers = MockDataService.getInstance().getContacts();

        list.clear();
        for (Users user : allUsers) {
            if (user.getUserID() != null && !user.getUserID().equals(currentUserId)) {
                list.add(user);
            }
        }

        adapter = new ContactsAdapter(list, ContactsActivity.this);
        binding.recyclerView.setAdapter(adapter);
    }
}