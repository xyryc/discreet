package com.example.chatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.model.OnboardingSlide;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private final List<OnboardingSlide> slides;

    public OnboardingAdapter(List<OnboardingSlide> slides) {
        this.slides = slides;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_slide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnboardingSlide slide = slides.get(position);
        holder.imgIcon.setImageResource(slide.getIconRes());
        holder.tvBadge.setText(slide.getBadgeText());
        holder.tvTitle.setText(slide.getTitle());
        holder.tvSubtitle.setText(slide.getSubtitle());
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgIcon;
        private final TextView tvBadge;
        private final TextView tvTitle;
        private final TextView tvSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_slide_icon);
            tvBadge = itemView.findViewById(R.id.tv_slide_badge);
            tvTitle = itemView.findViewById(R.id.tv_slide_title);
            tvSubtitle = itemView.findViewById(R.id.tv_slide_subtitle);
        }
    }
}
