package com.example.chatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.model.User;
import com.example.chatapplication.view.chats.ChatsActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<User> list;
    private final List<User> fullList;
    private final Context context;

    public ContactsAdapter(List<User> list, Context context) {
        this.list = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        this.context = context;
    }

    public void filter(String query) {
        list.clear();
        if (query == null || query.trim().isEmpty()) {
            list.addAll(fullList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (User user : fullList) {
                if (user.getUserName() != null && user.getUserName().toLowerCase().contains(lowerQuery)) {
                    list.add(user);
                } else if (user.getBio() != null && user.getBio().toLowerCase().contains(lowerQuery)) {
                    list.add(user);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = list.get(position);

        holder.username.setText(user.getUserName());
        holder.desc.setText(user.getBio() != null && !user.getBio().isEmpty() ? user.getBio() : "Available on Discreet");

        if (user.getImageProfile() != null && !user.getImageProfile().isEmpty()) {
            Glide.with(context).load(user.getImageProfile()).into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.icon_person);
        }

        View.OnClickListener openChatListener = v -> {
            Intent intent = new Intent(context, ChatsActivity.class);
            intent.putExtra("userID", user.getUserID());
            intent.putExtra("userName", user.getUserName());
            intent.putExtra("userProfile", user.getImageProfile());
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(openChatListener);
        if (holder.btnStartChat != null) {
            holder.btnStartChat.setOnClickListener(openChatListener);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProfile;
        private final TextView username, desc;
        private final View btnStartChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.tv_username);
            desc = itemView.findViewById(R.id.tv_desc);
            btnStartChat = itemView.findViewById(R.id.btn_start_chat);
        }
    }
}
