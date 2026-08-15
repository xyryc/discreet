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
import com.example.chatapplication.model.ChatItem;
import com.example.chatapplication.view.chats.ChatsActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private List<ChatItem> list;
    private final List<ChatItem> fullList;
    private final Context context;

    public ChatListAdapter(List<ChatItem> list, Context context) {
        this.list = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        this.context = context;
    }

    public void updateList(List<ChatItem> newList) {
        this.fullList.clear();
        this.fullList.addAll(newList);
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        list.clear();
        if (query == null || query.trim().isEmpty()) {
            list.addAll(fullList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (ChatItem item : fullList) {
                if (item.getUserName() != null && item.getUserName().toLowerCase().contains(lowerQuery)) {
                    list.add(item);
                } else if (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerQuery)) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chat = list.get(position);

        holder.tvUsername.setText(chat.getUserName());
        holder.tvDesc.setText(chat.getDescription());
        holder.tvDate.setText(chat.getDate());

        if (chat.getUrlProfile() != null && !chat.getUrlProfile().isEmpty()) {
            Glide.with(context).load(chat.getUrlProfile()).into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.icon_person);
        }

        // Unread badge logic
        if (chat.getUnreadCount() > 0) {
            holder.tvUnreadBadge.setVisibility(View.VISIBLE);
            holder.tvUnreadBadge.setText(String.valueOf(chat.getUnreadCount()));
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.neu_accent));
        } else {
            holder.tvUnreadBadge.setVisibility(View.GONE);
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.neu_text_muted));
        }

        // Online presence dot
        if (holder.viewOnlineDot != null) {
            holder.viewOnlineDot.setVisibility(chat.isOnline() ? View.VISIBLE : View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatsActivity.class);
            intent.putExtra("userID", chat.getUserID());
            intent.putExtra("userName", chat.getUserName());
            intent.putExtra("userProfile", chat.getUrlProfile());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProfile;
        private final TextView tvUsername, tvDesc, tvDate, tvUnreadBadge;
        private final View viewOnlineDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvUnreadBadge = itemView.findViewById(R.id.tv_unread_badge);
            viewOnlineDot = itemView.findViewById(R.id.view_online_dot);
        }
    }
}
