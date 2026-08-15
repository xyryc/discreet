package com.example.chatapplication.common;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {
    private static final String PREF_THEME = "discreet_theme_pref";
    private static final String KEY_CURRENT_THEME = "current_theme";

    public static final String THEME_LIGHT_NEU = "light_neu";
    public static final String THEME_EMERALD = "emerald";
    public static final String THEME_CYBER_DARK = "cyber_dark";
    public static final String THEME_ROYAL_INDIGO = "royal_indigo";

    private final SharedPreferences preferences;
    private static ThemeManager instance;

    private ThemeManager(Context context) {
        preferences = context.getSharedPreferences(PREF_THEME, Context.MODE_PRIVATE);
    }

    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context.getApplicationContext());
        }
        return instance;
    }

    public String getCurrentTheme() {
        return preferences.getString(KEY_CURRENT_THEME, THEME_LIGHT_NEU);
    }

    public void setTheme(String themeKey) {
        preferences.edit().putString(KEY_CURRENT_THEME, themeKey).apply();
    }
}
