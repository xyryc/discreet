package com.example.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.data.SessionManager;
import com.example.chatapplication.model.chat.Chats;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private final List<Chats> list;
    private final Context context;
    private final String currentUserId;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
        this.currentUserId = SessionManager.getInstance(context).getUserId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        private final TextView tvTime;
        private final ImageView imageProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.tv_text_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            imageProfile = itemView.findViewById(R.id.image_profile);
        }

        void bind(Chats chats) {
            if (textMessage != null) {
                textMessage.setText(chats.getTextMessage());
            }
            if (tvTime != null && chats.getDateTime() != null && !chats.getDateTime().isEmpty()) {
                tvTime.setText(chats.getDateTime());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender() != null && list.get(position).getSender().equals(currentUserId)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
