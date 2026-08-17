package com.example.chatapplication.model;

public class OnboardingSlide {
    private final int iconRes;
    private final String title;
    private final String subtitle;
    private final String badgeText;

    public OnboardingSlide(int iconRes, String title, String subtitle, String badgeText) {
        this.iconRes = iconRes;
        this.title = title;
        this.subtitle = subtitle;
        this.badgeText = badgeText;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBadgeText() {
        return badgeText;
    }
}
