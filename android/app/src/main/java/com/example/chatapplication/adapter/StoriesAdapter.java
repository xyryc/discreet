package com.example.chatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.model.user.Users;
import com.example.chatapplication.view.chats.ChatsActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {

    private final List<Users> usersList;
    private final Context context;

    public StoriesAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_story_item, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Users user = usersList.get(position);
        
        String firstName = user.getUserName().split(" ")[0];
        holder.tvStoryName.setText(firstName);

        Glide.with(context)
                .load(user.getImageProfile())
                .placeholder(R.drawable.icon_person)
                .into(holder.imageStoryAvatar);

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
        return usersList.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageStoryAvatar;
        TextView tvStoryName;
        View onlineIndicator;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageStoryAvatar = itemView.findViewById(R.id.image_story_avatar);
            tvStoryName = itemView.findViewById(R.id.tv_story_name);
            onlineIndicator = itemView.findViewById(R.id.story_online_indicator);
        }
    }
}
