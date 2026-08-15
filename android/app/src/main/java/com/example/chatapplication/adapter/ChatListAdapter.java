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
import com.example.chatapplication.model.ChatList;
import com.example.chatapplication.view.chats.ChatsActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<ChatList> list;
    private final List<ChatList> fullList;
    private final Context context;

    public ChatListAdapter(List<ChatList> list, Context context) {
        this.list = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        this.context = context;
    }

    public void updateList(List<ChatList> newList) {
        this.list = new ArrayList<>(newList);
        this.fullList.clear();
        this.fullList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            list = new ArrayList<>(fullList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            List<ChatList> filtered = new ArrayList<>();
            for (ChatList item : fullList) {
                if (item.getUserName().toLowerCase().contains(lowerQuery) ||
                    (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerQuery))) {
                    filtered.add(item);
                }
            }
            list = filtered;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ChatList chat = list.get(position);

        holder.tvName.setText(chat.getUserName());
        holder.tvDesc.setText(chat.getDescription());
        holder.tvDate.setText(chat.getDate());

        // Online status indicator
        holder.onlineIndicator.setVisibility(chat.isOnline() ? View.VISIBLE : View.GONE);

        // Read status checkmark
        if (chat.isRead()) {
            holder.iconReadStatus.setVisibility(View.VISIBLE);
            holder.iconReadStatus.setImageResource(R.drawable.ic_baseline_done_all_24);
        } else {
            holder.iconReadStatus.setVisibility(View.GONE);
        }

        // Unread badge count
        if (chat.getUnreadCount() > 0) {
            holder.tvUnreadBadge.setVisibility(View.VISIBLE);
            holder.tvUnreadBadge.setText(String.valueOf(chat.getUnreadCount()));
        } else {
            holder.tvUnreadBadge.setVisibility(View.GONE);
        }

        // Load avatar with Glide
        Glide.with(context)
                .load(chat.getUrlProfile())
                .placeholder(R.drawable.icon_person)
                .into(holder.profile);

        // Click action -> Opens direct chat conversation
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

    public static class Holder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvDesc, tvDate, tvUnreadBadge;
        private final ShapeableImageView profile;
        private final View onlineIndicator;
        private final ImageView iconReadStatus;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvUnreadBadge = itemView.findViewById(R.id.tv_unread_badge);
            profile = itemView.findViewById(R.id.image_profile);
            onlineIndicator = itemView.findViewById(R.id.view_online_indicator);
            iconReadStatus = itemView.findViewById(R.id.icon_read_status);
        }
    }
}
