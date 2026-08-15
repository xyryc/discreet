package com.example.chatapplication.view.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ContactsAdapter;
import com.example.chatapplication.data.MockDataService;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.databinding.ActivityContactsBinding;
import com.example.chatapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;
    private final List<User> list = new ArrayList<>();
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnRefresh.setOnClickListener(v ->
                Toast.makeText(this, "Refreshing encrypted contacts...", Toast.LENGTH_SHORT).show()
        );

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initSearch();
        getContactList();
    }

    private void initSearch() {
        binding.edSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (adapter != null) {
                    adapter.filter(query);
                }
                binding.btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnClearSearch.setOnClickListener(v -> {
            binding.edSearchContacts.setText("");
        });
    }

    private void getContactList() {
        String currentUserId = SessionManager.getInstance(this).getUserId();
        List<User> allUsers = MockDataService.getInstance().getContacts();

        list.clear();
        for (User user : allUsers) {
            if (user.getUserID() != null && !user.getUserID().equals(currentUserId)) {
                list.add(user);
            }
        }

        binding.tvContactsCount.setText(list.size() + " contacts on Discreet");

        adapter = new ContactsAdapter(list, ContactsActivity.this);
        binding.recyclerView.setAdapter(adapter);
    }
}