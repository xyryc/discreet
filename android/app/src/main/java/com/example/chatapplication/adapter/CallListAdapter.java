package com.example.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.model.CallItem;

import java.util.ArrayList;
import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ViewHolder> {
    private List<CallItem> list;
    private final List<CallItem> fullList;
    private final Context context;

    public CallListAdapter(List<CallItem> list, Context context) {
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
            for (CallItem item : fullList) {
                if (item.getUserName() != null && item.getUserName().toLowerCase().contains(lowerQuery)) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterMissedOnly(boolean missedOnly) {
        list.clear();
        if (!missedOnly) {
            list.addAll(fullList);
        } else {
            for (CallItem item : fullList) {
                if ("MISSED".equalsIgnoreCase(item.getCallType())) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateList(List<CallItem> newList) {
        this.fullList.clear();
        this.fullList.addAll(newList);
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_call, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CallItem call = list.get(position);

        holder.tvName.setText(call.getUserName());

        if (call.getUrlProfile() != null && !call.getUrlProfile().isEmpty()) {
            Glide.with(context).load(call.getUrlProfile()).into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.icon_person);
        }

        // Call Type Arrow indicator
        if ("MISSED".equalsIgnoreCase(call.getCallType())) {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_received_24);
            holder.imgCallDirection.setColorFilter(context.getResources().getColor(R.color.neu_danger));
            holder.tvCallInfo.setText(call.getDate() + " • Missed");
            holder.tvCallInfo.setTextColor(context.getResources().getColor(R.color.neu_danger));
        } else if ("OUTGOING".equalsIgnoreCase(call.getCallType())) {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_made_24);
            holder.imgCallDirection.setColorFilter(context.getResources().getColor(R.color.neu_accent));
            holder.tvCallInfo.setText(call.getDate() + " • " + call.getDuration());
            holder.tvCallInfo.setTextColor(context.getResources().getColor(R.color.neu_text_secondary));
        } else {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_received_24);
            holder.imgCallDirection.setColorFilter(context.getResources().getColor(R.color.neu_success));
            holder.tvCallInfo.setText(call.getDate() + " • " + call.getDuration());
            holder.tvCallInfo.setTextColor(context.getResources().getColor(R.color.neu_text_secondary));
        }

        // Audio vs Video Action Icon
        if (call.isVideo()) {
            holder.imgCallMediaType.setImageResource(R.drawable.ic_baseline_videocam_24);
            holder.iconActionCall.setImageResource(R.drawable.ic_baseline_videocam_24);
        } else {
            holder.imgCallMediaType.setImageResource(R.drawable.ic_baseline_call_24);
            holder.iconActionCall.setImageResource(R.drawable.ic_baseline_call_24);
        }

        // Click Call Action
        holder.btnCallAction.setOnClickListener(v -> {
            String type = call.isVideo() ? "Encrypted Video Call" : "Encrypted Voice Call";
            Toast.makeText(context, "Starting " + type + " with " + call.getUserName() + "...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageProfile, imgCallDirection, imgCallMediaType, iconActionCall;
        private final TextView tvName, tvCallInfo;
        private final View btnCallAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            imgCallDirection = itemView.findViewById(R.id.img_call_direction);
            imgCallMediaType = itemView.findViewById(R.id.img_call_media_type);
            tvCallInfo = itemView.findViewById(R.id.tv_call_info);
            btnCallAction = itemView.findViewById(R.id.btn_call_action);
            iconActionCall = itemView.findViewById(R.id.icon_action_call);
        }
    }
}
