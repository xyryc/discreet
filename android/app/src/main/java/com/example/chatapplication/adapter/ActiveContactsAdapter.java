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
import com.example.chatapplication.model.user.Users;
import com.example.chatapplication.view.chats.ChatsActivity;

import java.util.List;

public class ActiveContactsAdapter extends RecyclerView.Adapter<ActiveContactsAdapter.ViewHolder> {
    private final List<Users> list;
    private final Context context;

    public ActiveContactsAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_active_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = list.get(position);
        holder.username.setText(user.getUserName());

        if (user.getImageProfile() != null && !user.getImageProfile().isEmpty()) {
            Glide.with(context).load(user.getImageProfile()).into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.icon_person);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatsActivity.class);
            intent.putExtra("userID", user.getUserID());
            intent.putExtra("userName", user.getUserName());
            intent.putExtra("userProfile", user.getImageProfile());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProfile;
        private final TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.tv_username);
        }
    }
}
