package com.example.chatapplication.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.model.CallList;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.Holder> {
    private List<CallList> list;
    private final List<CallList> fullList;
    private final Context context;

    public CallListAdapter(List<CallList> list, Context context) {
        this.list = new ArrayList<>(list);
        this.fullList = new ArrayList<>(list);
        this.context = context;
    }

    public void updateList(List<CallList> newList) {
        this.list = new ArrayList<>(newList);
        this.fullList.clear();
        this.fullList.addAll(newList);
        notifyDataSetChanged();
    }

    public void filterMissedOnly(boolean missedOnly) {
        if (!missedOnly) {
            list = new ArrayList<>(fullList);
        } else {
            List<CallList> filtered = new ArrayList<>();
            for (CallList item : fullList) {
                if ("MISSED".equalsIgnoreCase(item.getCallType())) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CallList call = list.get(position);

        holder.tvName.setText(call.getUserName());

        // Direction Arrow & Tint
        String type = call.getCallType() != null ? call.getCallType().toUpperCase() : "INCOMING";
        if ("MISSED".equals(type)) {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_missed_24);
            holder.imgCallDirection.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.neu_danger)));
        } else if ("OUTGOING".equals(type)) {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_made_24);
            holder.imgCallDirection.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.neu_accent)));
        } else {
            holder.imgCallDirection.setImageResource(R.drawable.ic_baseline_call_received_24);
            holder.imgCallDirection.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.neu_success)));
        }

        // Media Type Icon (Audio vs Video)
        if (call.isVideo()) {
            holder.imgCallMediaType.setImageResource(R.drawable.ic_baseline_videocam_24);
            holder.iconActionCall.setImageResource(R.drawable.ic_baseline_videocam_24);
        } else {
            holder.imgCallMediaType.setImageResource(R.drawable.ic_baseline_call_24);
            holder.iconActionCall.setImageResource(R.drawable.ic_baseline_call_24);
        }

        // Call Info (Date • Duration)
        String duration = call.getDuration() != null ? call.getDuration() : "";
        if (!duration.isEmpty()) {
            holder.tvCallInfo.setText(call.getDate() + " • " + duration);
        } else {
            holder.tvCallInfo.setText(call.getDate());
        }

        // Load Avatar
        Glide.with(context)
                .load(call.getUrlProfile())
                .placeholder(R.drawable.icon_person)
                .into(holder.profile);

        // Action Button -> Initiate Call
        holder.btnCallAction.setOnClickListener(v -> {
            String mediaStr = call.isVideo() ? "Video" : "Voice";
            Toast.makeText(context, "Initiating encrypted " + mediaStr + " call to " + call.getUserName() + "...", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {
            String mediaStr = call.isVideo() ? "Video" : "Voice";
            Toast.makeText(context, "Call details with " + call.getUserName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvCallInfo;
        private final ShapeableImageView profile;
        private final ImageView imgCallDirection, imgCallMediaType, iconActionCall;
        private final FrameLayout btnCallAction;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCallInfo = itemView.findViewById(R.id.tv_call_info);
            profile = itemView.findViewById(R.id.image_profile);
            imgCallDirection = itemView.findViewById(R.id.img_call_direction);
            imgCallMediaType = itemView.findViewById(R.id.img_call_media_type);
            iconActionCall = itemView.findViewById(R.id.icon_action_call);
            btnCallAction = itemView.findViewById(R.id.btn_call_action);
        }
    }
}
